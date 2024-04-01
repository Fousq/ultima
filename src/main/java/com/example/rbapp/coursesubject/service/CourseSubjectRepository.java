package com.example.rbapp.coursesubject.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.coursesubject.controller.api.RecentCourseSubjectResponse;
import com.example.rbapp.coursesubject.service.recordmapper.RecentCourseSubjectResponseRecordMapper;
import com.example.rbapp.jooq.codegen.tables.records.CourseSubjectRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;
import static org.jooq.impl.DSL.count;

@Repository
@RequiredArgsConstructor
public class CourseSubjectRepository {

    private final DSLContext dslContext;
    private final RecentCourseSubjectResponseRecordMapper recentCourseSubjectResponseRecordMapper;

    public List<CourseSubjectRecord> findAllByCourseId(Long courseId) {
        return dslContext.select().from(COURSE_SUBJECT)
                .where(COURSE_SUBJECT.COURSE_ID.eq(courseId))
                .fetchInto(CourseSubjectRecord.class);
    }

    public void batchCreate(List<CourseSubjectRecord> courseSubjectRecords) {
        var insert = courseSubjectRecords.stream()
                .map(courseSubjectRecord ->
                        dslContext
                                .insertInto(COURSE_SUBJECT)
                                .set(COURSE_SUBJECT.TITLE, courseSubjectRecord.getTitle())
                                .set(COURSE_SUBJECT.DESCRIPTION, courseSubjectRecord.getDescription())
                                .set(COURSE_SUBJECT.FILES, courseSubjectRecord.getFiles())
                                .set(COURSE_SUBJECT.COURSE_ID, courseSubjectRecord.getCourseId())
                                .set(COURSE_SUBJECT.COMPLETED, courseSubjectRecord.getCompleted())
                                .set(COURSE_SUBJECT.DURATION, courseSubjectRecord.getDuration())
                                .set(COURSE_SUBJECT.START_AT, courseSubjectRecord.getStartAt())
                ).toList();
        dslContext.batch(insert).execute();
    }

    public void deleteAllByCourseId(Long courseId) {
        List<Long> courseSubjectIds = dslContext.select(COURSE_SUBJECT.ID).from(COURSE_SUBJECT)
                .where(COURSE_SUBJECT.COURSE_ID.eq(courseId))
                .fetchInto(Long.class);
        deleteAll(courseSubjectIds);
    }

    public void batchUpdate(List<CourseSubjectRecord> courseSubjectRecords) {
        dslContext.batchUpdate(courseSubjectRecords).execute();
    }

    public void deleteAll(List<Long> courseSubjectIds) {
        List<Long> homeworkIds = dslContext.select(HOMEWORK.ID).from(HOMEWORK)
                .where(HOMEWORK.COURSE_SUBJECT_ID.in(courseSubjectIds))
                .fetchInto(Long.class);
        dslContext.deleteFrom(STUDENT_HOMEWORK).where(STUDENT_HOMEWORK.HOMEWORK_ID.in(homeworkIds)).execute();
        dslContext.deleteFrom(HOMEWORK).where(HOMEWORK.ID.in(homeworkIds)).execute();
        dslContext.deleteFrom(COURSE_SUBJECT).where(COURSE_SUBJECT.ID.in(courseSubjectIds)).execute();
    }

    public void updateCompleted(Long id, Boolean completed) {
        dslContext.update(COURSE_SUBJECT)
                .set(COURSE_SUBJECT.COMPLETED, completed)
                .where(COURSE_SUBJECT.ID.eq(id))
                .execute();
    }

    public Optional<CourseSubjectRecord> findById(Long id) {
        return dslContext.selectFrom(COURSE_SUBJECT)
                .where(COURSE_SUBJECT.ID.eq(id))
                .fetchOptional();
    }

    public List<CourseSubjectRecord> findAllByStartAtBetween(ZonedDateTime yesterday, ZonedDateTime today) {
        return dslContext.selectFrom(COURSE_SUBJECT)
                .where(COURSE_SUBJECT.START_AT.between(yesterday.toLocalDateTime(), today.toLocalDateTime()))
                .fetchInto(CourseSubjectRecord.class);
    }

    public void createStudentCourseSubject(List<Long> studentIds, Long courseSubjectId) {
        var insert = studentIds.stream().map(studentId -> dslContext.insertInto(STUDENT_COURSE_SUBJECT)
                .set(STUDENT_COURSE_SUBJECT.STUDENT_ID, studentId)
                .set(STUDENT_COURSE_SUBJECT.COURSE_SUBJECT_ID, courseSubjectId)
        ).toList();
        dslContext.batch(insert).execute();
    }

    public void deleteStudentCourseSubject(List<Long> studentIds, Long courseSubjectId) {
        dslContext.deleteFrom(STUDENT_COURSE_SUBJECT)
                .where(STUDENT_COURSE_SUBJECT.STUDENT_ID.in(studentIds))
                .and(STUDENT_COURSE_SUBJECT.COURSE_SUBJECT_ID.eq(courseSubjectId))
                .execute();
    }

    public void createStudentCourseSubjectByUserId(Long id, Long userId) {
        Long studentId = dslContext.select(STUDENT.ID).from(STUDENT)
                .innerJoin(APP_USER).on(APP_USER.ID.eq(STUDENT.USER_ID))
                .where(APP_USER.ID.eq(userId))
                .fetchOptionalInto(Long.class)
                .orElseThrow(() -> new NotFoundException("Student id not found"));
        createStudentCourseSubject(List.of(studentId), id);
    }

    public Optional<RecentCourseSubjectResponse> findStudentRecentByUserId(Long userId) {
        Optional<Long> optionalStudentId = dslContext.select(STUDENT.ID).from(STUDENT)
                .where(STUDENT.USER_ID.eq(userId))
                .fetchOptionalInto(Long.class);
        return optionalStudentId.flatMap(studentId ->
                dslContext.select(COURSE_SUBJECT.ID, COURSE_SUBJECT.TITLE, COURSE_SUBJECT.START_AT, COURSE.LESSON_LINK)
                        .from(COURSE_SUBJECT)
                        .innerJoin(COURSE).on(COURSE.ID.eq(COURSE_SUBJECT.COURSE_ID))
                        .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.COURSE_ID.eq(COURSE_SUBJECT.COURSE_ID))
                        .where(STUDENT_COURSE.STUDENT_ID.eq(studentId))
                        .and(COURSE_SUBJECT.START_AT.lessOrEqual(LocalDateTime.now(DateTimeConstant.ALMATY).plusHours(1)))
                        .orderBy(COURSE_SUBJECT.START_AT)
                        .limit(1)
                        .fetchOptional(recentCourseSubjectResponseRecordMapper)
        );
    }

    public Optional<RecentCourseSubjectResponse> findTeacherRecentByUserId(Long userId) {
        Optional<Long> optionalTeacherId = dslContext.select(TEACHER.ID).from(TEACHER)
                .where(TEACHER.USER_ID.eq(userId))
                .fetchOptionalInto(Long.class);
        return optionalTeacherId.flatMap(teacherId ->
                dslContext.select(COURSE_SUBJECT.ID, COURSE_SUBJECT.TITLE, COURSE_SUBJECT.START_AT, COURSE.LESSON_LINK)
                        .from(COURSE_SUBJECT)
                        .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.COURSE_ID.eq(COURSE_SUBJECT.COURSE_ID))
                        .innerJoin(COURSE).on(COURSE.ID.eq(TEACHER_COURSE.COURSE_ID))
                        .where(TEACHER_COURSE.TEACHER_ID.eq(teacherId))
                        .and(COURSE_SUBJECT.START_AT.lessOrEqual(LocalDateTime.now().plusHours(1)))
                        .orderBy(COURSE_SUBJECT.START_AT)
                        .limit(1)
                        .fetchOptional(recentCourseSubjectResponseRecordMapper)
        );
    }

    public Optional<RecentCourseSubjectResponse> findMostRecent() {
        return dslContext.select(COURSE_SUBJECT.ID, COURSE_SUBJECT.TITLE, COURSE_SUBJECT.START_AT, COURSE.LESSON_LINK)
                .from(COURSE_SUBJECT)
                .innerJoin(COURSE).on(COURSE.ID.eq(COURSE_SUBJECT.COURSE_ID))
                .orderBy(COURSE_SUBJECT.START_AT)
                .limit(1)
                .fetchOptional(recentCourseSubjectResponseRecordMapper);
    }

    public Integer countSubjectsByCourseTypeForTeacher(Long teacherId, String type) {
        return dslContext.selectCount().from(COURSE_SUBJECT)
                        .innerJoin(COURSE).on(COURSE.ID.eq(COURSE_SUBJECT.COURSE_ID))
                        .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.COURSE_ID.eq(COURSE_SUBJECT.COURSE_ID))
                        .where(COURSE.TYPE.eq(type))
                        .and(TEACHER_COURSE.TEACHER_ID.eq(teacherId))
                .fetchSingleInto(Integer.class);
    }

    public Integer countCompletedSubjectsByCourseTypeForTeacher(Long teacherId, String type) {
        return dslContext.selectCount().from(COURSE_SUBJECT)
                .innerJoin(COURSE).on(COURSE.ID.eq(COURSE_SUBJECT.COURSE_ID))
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.COURSE_ID.eq(COURSE_SUBJECT.COURSE_ID))
                .where(COURSE.TYPE.eq(type))
                .and(COURSE_SUBJECT.COMPLETED.isTrue())
                .and(TEACHER_COURSE.TEACHER_ID.eq(teacherId))
                .fetchSingleInto(Integer.class);
    }
}
