package com.example.rbapp.course.service;

import com.example.rbapp.course.controller.api.CourseResponse;
import com.example.rbapp.course.controller.api.StudentCourseResponse;
import com.example.rbapp.course.service.recordmapper.StudentCourseResponseRecordMapper;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private final DSLContext dslContext;
    private final StudentCourseResponseRecordMapper studentCourseResponseRecordMapper;

    public List<CourseRecord> findAll() {
        return dslContext.select().from(COURSE).fetchInto(CourseRecord.class);
    }

    public Optional<CourseRecord> findById(Long id) {
        return dslContext.selectFrom(COURSE)
                .where(COURSE.ID.eq(id))
                .fetchOptional();
    }

    public Long create(CourseRecord courseRecord) {
        return dslContext.insertInto(COURSE)
                .set(COURSE.TITLE, courseRecord.getTitle())
                .set(COURSE.LESSON_LINK, courseRecord.getLessonLink())
                .set(COURSE.DURATION, courseRecord.getDuration())
                .set(COURSE.TYPE, courseRecord.getType())
                .returningResult(COURSE.ID)
                .fetchSingleInto(Long.class);
    }

    public void update(CourseRecord courseRecord) {
        dslContext.update(COURSE)
                .set(COURSE.TITLE, courseRecord.getTitle())
                .set(COURSE.LESSON_LINK, courseRecord.getLessonLink())
                .set(COURSE.DURATION, courseRecord.getDuration())
                .set(COURSE.TYPE, courseRecord.getType())
                .where(COURSE.ID.eq(courseRecord.getId()))
                .execute();
    }

    public void deleteById(Long id) {
        List<Long> courseSubjectIdList = dslContext.select(COURSE_SUBJECT.ID).from(COURSE_SUBJECT)
                .where(COURSE_SUBJECT.COURSE_ID.eq(id))
                .fetchInto(Long.class);
        dslContext.deleteFrom(STUDENT_COURSE_SUBJECT)
                .where(STUDENT_COURSE_SUBJECT.COURSE_SUBJECT_ID.in(courseSubjectIdList))
                .execute();
        List<Long> homeworkIdList = dslContext.select(HOMEWORK.ID).from(HOMEWORK)
                .where(HOMEWORK.COURSE_SUBJECT_ID.in(courseSubjectIdList))
                .fetchInto(Long.class);
        dslContext.deleteFrom(STUDENT_HOMEWORK)
                .where(STUDENT_HOMEWORK.HOMEWORK_ID.in(homeworkIdList))
                .execute();
        dslContext.deleteFrom(HOMEWORK)
                .where(HOMEWORK.ID.in(homeworkIdList))
                .execute();
        dslContext.deleteFrom(COURSE_SUBJECT)
                .where(COURSE_SUBJECT.ID.in(courseSubjectIdList))
                .execute();
        dslContext.deleteFrom(STUDENT_COURSE)
                .where(STUDENT_COURSE.COURSE_ID.eq(id))
                .execute();
        dslContext.deleteFrom(TEACHER_COURSE)
                .where(TEACHER_COURSE.COURSE_ID.eq(id))
                .execute();
        dslContext.deleteFrom(COURSE).where(COURSE.ID.eq(id)).execute();
    }

    public Optional<Long> findIdByTitle(String title) {
        return dslContext.select(COURSE.ID)
                .from(COURSE)
                .where(COURSE.TITLE.eq(title))
                .fetchOptionalInto(Long.class);
    }

    public List<StudentCourseResponse> findAllByUserId(Long userId) {
        return dslContext.select(COURSE.asterisk(), TEACHER.asterisk())
                .from(COURSE)
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.COURSE_ID.eq(COURSE.ID))
                .innerJoin(TEACHER).on(TEACHER.ID.eq(TEACHER_COURSE.TEACHER_ID))
                .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.COURSE_ID.eq(COURSE.ID))
                .innerJoin(STUDENT).on(STUDENT.ID.eq(STUDENT_COURSE.STUDENT_ID))
                .where(STUDENT.USER_ID.eq(userId))
                .fetch(studentCourseResponseRecordMapper);
    }

    public List<CourseRecord> findAllByStudentId(Long studentId) {
        return dslContext.select(COURSE.asterisk()).from(COURSE)
                .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.COURSE_ID.eq(COURSE.ID))
                .where(STUDENT_COURSE.STUDENT_ID.eq(studentId))
                .fetchInto(CourseRecord.class);
    }

    public List<CourseRecord> findAllByTeacherId(Long teacherId) {
        return dslContext.select(COURSE.asterisk()).from(COURSE)
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.COURSE_ID.eq(COURSE.ID))
                .where(TEACHER_COURSE.TEACHER_ID.eq(teacherId))
                .fetchInto(CourseRecord.class);
    }
}
