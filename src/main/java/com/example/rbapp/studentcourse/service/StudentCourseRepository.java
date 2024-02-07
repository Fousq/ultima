package com.example.rbapp.studentcourse.service;

import com.example.rbapp.jooq.codegen.tables.records.StudentCourseRecord;
import com.example.rbapp.studentcourse.entity.StudentCourse;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.rbapp.jooq.codegen.Tables.*;


@Repository
@RequiredArgsConstructor
public class StudentCourseRepository {

    private final DSLContext dslContext;
    private final StudentCourseRecordMapper studentCourseRecordMapper;


    public void create(StudentCourseRecord studentCourse) {
        dslContext.insertInto(STUDENT_COURSE)
                .set(STUDENT_COURSE.STUDENT_ID, studentCourse.getStudentId())
                .set(STUDENT_COURSE.COURSE_ID, studentCourse.getCourseId())
                .execute();
    }

    public List<StudentCourse> findAllByCourseId(Long courseId) {
        return dslContext.select().from(STUDENT_COURSE)
                .innerJoin(STUDENT).on(STUDENT.ID.eq(STUDENT_COURSE.STUDENT_ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(STUDENT_COURSE.COURSE_ID))
                .where(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .fetch(studentCourseRecordMapper);
    }

    public void batchCreate(List<StudentCourseRecord> studentCourseRecordList) {
        var insert = studentCourseRecordList.stream()
                .map(studentCourseRecord ->
                        dslContext.insertInto(STUDENT_COURSE)
                                .set(STUDENT_COURSE.STUDENT_ID, studentCourseRecord.getStudentId())
                                .set(STUDENT_COURSE.COURSE_ID, studentCourseRecord.getCourseId())
                ).toList();
        dslContext.batch(insert).execute();
    }

    public void deleteStudentCourseListByCourseId(Long courseId) {
        dslContext.deleteFrom(STUDENT_COURSE)
                .where(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .execute();
    }

    public void deleteStudentCourseListByStudentIdsAndCourseId(List<Long> studentIds, Long courseId) {
        dslContext.deleteFrom(STUDENT_COURSE)
                .where(STUDENT_COURSE.STUDENT_ID.in(studentIds))
                .and(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .execute();
    }

    public StudentCourse findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return dslContext.select().from(STUDENT_COURSE)
                .innerJoin(STUDENT).on(STUDENT.ID.eq(STUDENT_COURSE.STUDENT_ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(STUDENT_COURSE.COURSE_ID))
                .where(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .and(STUDENT_COURSE.STUDENT_ID.eq(studentId))
                .fetchSingle(studentCourseRecordMapper);
    }
}
