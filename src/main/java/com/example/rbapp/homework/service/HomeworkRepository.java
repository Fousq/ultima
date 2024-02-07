package com.example.rbapp.homework.service;

import com.example.rbapp.homework.controller.api.StudentHomeworkDetailsResponse;
import com.example.rbapp.homework.entity.StudentHomework;
import com.example.rbapp.jooq.codegen.tables.records.HomeworkRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentHomeworkRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Repository
@RequiredArgsConstructor
public class HomeworkRepository {

    private final DSLContext dslContext;
    private final StudentHomeworkRecordMapper studentHomeworkRecordMapper;
    private final StudentHomeworkDetailsRecordMapper studentHomeworkDetailsRecordMapper;


    public List<HomeworkRecord> findAll() {
        return dslContext.selectFrom(HOMEWORK).fetchInto(HomeworkRecord.class);
    }

    public Optional<HomeworkRecord> findById(Long id) {
        return dslContext.selectFrom(HOMEWORK)
                .where(HOMEWORK.ID.eq(id))
                .fetchOptional();
    }

    public Long create(HomeworkRecord homework) {
        return dslContext.insertInto(HOMEWORK)
                .set(HOMEWORK.TITLE, homework.getTitle())
                .set(HOMEWORK.DESCRIPTION, homework.getDescription())
                .set(HOMEWORK.FILES, homework.getFiles())
                .set(HOMEWORK.COURSE_SUBJECT_ID, homework.getCourseSubjectId())
            .returningResult(HOMEWORK.ID)
            .fetchSingleInto(Long.class);
    }

    public void update(HomeworkRecord homework) {
        Query updateQuery = createUpdateQuery(homework);
        dslContext.execute(updateQuery);
    }

    public void deleteById(Long id) {
        dslContext.deleteFrom(HOMEWORK).where(HOMEWORK.ID.eq(id)).execute();
    }

    public List<StudentHomework> findAllByCourseSubjectId(Long courseSubjectId) {
        return dslContext.select().from(HOMEWORK)
                .innerJoin(STUDENT_HOMEWORK).on(STUDENT_HOMEWORK.HOMEWORK_ID.eq(HOMEWORK.ID))
                .where(HOMEWORK.COURSE_SUBJECT_ID.eq(courseSubjectId))
                .fetch(studentHomeworkRecordMapper);
    }

    public List<HomeworkRecord> findAllByCourseIdAndStudentId(Long courseId, Long studentId) {
        return dslContext.select(HOMEWORK.fields()).from(HOMEWORK)
                .innerJoin(COURSE_SUBJECT).on(COURSE_SUBJECT.ID.eq(HOMEWORK.COURSE_SUBJECT_ID))
                .innerJoin(STUDENT_HOMEWORK).on(STUDENT_HOMEWORK.HOMEWORK_ID.eq(HOMEWORK.ID))
                .where(STUDENT_HOMEWORK.STUDENT_ID.eq(studentId))
                .and(COURSE_SUBJECT.COURSE_ID.eq(courseId))
                .fetchInto(HomeworkRecord.class);
    }

    public Optional<HomeworkRecord> findByCourseSubjectId(Long courseSubjectId) {
        List<HomeworkRecord> homeworkRecordList = dslContext.selectFrom(HOMEWORK)
                .where(HOMEWORK.COURSE_SUBJECT_ID.eq(courseSubjectId))
                .fetchInto(HomeworkRecord.class);
        return homeworkRecordList.isEmpty() ? Optional.empty() : Optional.of(homeworkRecordList.get(0));
    }

    public void batchUpdate(List<HomeworkRecord> homeworkRecordList) {
        var update = homeworkRecordList.stream().map(this::createUpdateQuery).toList();
        dslContext.batch(update).execute();
    }

    private Query createUpdateQuery(HomeworkRecord homework) {
        return dslContext.update(HOMEWORK)
                .set(HOMEWORK.TITLE, homework.getTitle())
                .set(HOMEWORK.DESCRIPTION, homework.getDescription())
                .set(HOMEWORK.FILES, homework.getFiles())
                .set(HOMEWORK.COURSE_SUBJECT_ID, homework.getCourseSubjectId())
                .where(HOMEWORK.ID.eq(homework.getId()));
    }

    public void createStudentHomework(StudentHomeworkRecord studentHomeworkRecord) {
        dslContext.insertInto(STUDENT_HOMEWORK).set(studentHomeworkRecord).execute();
    }

    public List<StudentHomeworkRecord> findAllByHomeworkId(Long homeworkId) {
        return dslContext.selectFrom(STUDENT_HOMEWORK)
                .where(STUDENT_HOMEWORK.HOMEWORK_ID.eq(homeworkId))
                .fetchInto(StudentHomeworkRecord.class);
    }

    public void deleteStudentHomeworkByStudentIdAndHomeworkId(List<Long> studentIds, Long homeworkId) {
        dslContext.deleteFrom(STUDENT_HOMEWORK)
                .where(STUDENT_HOMEWORK.STUDENT_ID.in(studentIds))
                .and(STUDENT_HOMEWORK.HOMEWORK_ID.eq(homeworkId))
                .execute();
    }

    public void addStudentHomeworkList(List<Long> studentIds, Long homeworkId) {
        var insert = studentIds.stream().map(studentId ->
                dslContext.insertInto(STUDENT_HOMEWORK)
                        .set(STUDENT_HOMEWORK.STUDENT_ID, studentId)
                        .set(STUDENT_HOMEWORK.HOMEWORK_ID, homeworkId)
        ).toList();
        dslContext.batch(insert).execute();
    }

    public void updateStudentHomework(StudentHomeworkRecord studentHomeworkRecord) {
        dslContext.update(STUDENT_HOMEWORK)
                .set(STUDENT_HOMEWORK.FILE, studentHomeworkRecord.getFile())
                .set(STUDENT_HOMEWORK.COMPLETED, studentHomeworkRecord.getCompleted())
                .set(STUDENT_HOMEWORK.IN_PROGRESS, studentHomeworkRecord.getInProgress())
                .set(STUDENT_HOMEWORK.FEEDBACK, studentHomeworkRecord.getFeedback())
                .where(STUDENT_HOMEWORK.HOMEWORK_ID.eq(studentHomeworkRecord.getHomeworkId()))
                .and(STUDENT_HOMEWORK.STUDENT_ID.eq(studentHomeworkRecord.getStudentId()))
                .execute();
    }

    public Optional<StudentHomeworkRecord> findStudentHomeworkByHomeworkIdAndStudentId(Long homeworkId, Long studentId) {
        return dslContext.selectFrom(STUDENT_HOMEWORK)
                .where(STUDENT_HOMEWORK.HOMEWORK_ID.eq(homeworkId))
                .and(STUDENT_HOMEWORK.STUDENT_ID.eq(studentId))
                .fetchOptional();
    }

    public List<StudentHomeworkDetailsResponse> findAllByCourseId(Long courseId) {
        return dslContext.select().from(STUDENT_HOMEWORK)
                .innerJoin(HOMEWORK).on(HOMEWORK.ID.eq(STUDENT_HOMEWORK.HOMEWORK_ID))
                .innerJoin(STUDENT).on(STUDENT.ID.eq(STUDENT_HOMEWORK.STUDENT_ID))
                .innerJoin(COURSE_SUBJECT).on(COURSE_SUBJECT.ID.eq(HOMEWORK.COURSE_SUBJECT_ID))
                .where(COURSE_SUBJECT.COURSE_ID.eq(courseId))
                .fetch(studentHomeworkDetailsRecordMapper);
    }

    public void updateStudentHomework(List<StudentHomeworkRecord> studentHomeworkRecords) {
        var update = studentHomeworkRecords.stream()
                .map(studentHomeworkRecord -> dslContext.update(STUDENT_HOMEWORK)
                        .set(STUDENT_HOMEWORK.FILE, studentHomeworkRecord.getFile())
                        .set(STUDENT_HOMEWORK.COMPLETED, studentHomeworkRecord.getCompleted())
                        .set(STUDENT_HOMEWORK.IN_PROGRESS, studentHomeworkRecord.getInProgress())
                        .set(STUDENT_HOMEWORK.FEEDBACK, studentHomeworkRecord.getFeedback())
                        .where(STUDENT_HOMEWORK.HOMEWORK_ID.eq(studentHomeworkRecord.getHomeworkId()))
                        .and(STUDENT_HOMEWORK.STUDENT_ID.eq(studentHomeworkRecord.getStudentId())))
                .toList();

        dslContext.batch(update).execute();
    }

    public void batchCreateStudentHomework(List<StudentHomeworkRecord> studentHomeworkRecords) {
        dslContext.batchInsert(studentHomeworkRecords).execute();
    }
}
