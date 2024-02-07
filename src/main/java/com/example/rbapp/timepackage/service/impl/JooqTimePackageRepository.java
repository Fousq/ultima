package com.example.rbapp.timepackage.service.impl;

import com.example.rbapp.jooq.codegen.tables.records.TimePackageRecord;
import com.example.rbapp.timepackage.controller.api.CourseTimePackageResponse;
import com.example.rbapp.timepackage.service.CourseTimePackageResponseRecordMapper;
import com.example.rbapp.timepackage.service.TimePackageRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Repository
@RequiredArgsConstructor
public class JooqTimePackageRepository implements TimePackageRepository {

    private final DSLContext dslContext;

    private final CourseTimePackageResponseRecordMapper courseTimePackageResponseRecordMapper;

    public Long create(TimePackageRecord timePackageRecord) {
        return dslContext.insertInto(TIME_PACKAGE)
                .set(TIME_PACKAGE.AMOUNT_IN_MINUTES, timePackageRecord.getAmountInMinutes())
                .set(TIME_PACKAGE.TYPE, timePackageRecord.getType())
                .set(TIME_PACKAGE.STUDENT_ID, timePackageRecord.getStudentId())
                .set(TIME_PACKAGE.INITIAL_AMOUNT_IN_MINUTES, timePackageRecord.getAmountInMinutes())
                .returningResult(TIME_PACKAGE.ID)
                .fetchSingleInto(Long.class);
    }

    public Optional<TimePackageRecord> findById(Long id) {
        return dslContext.selectFrom(TIME_PACKAGE)
                .where(TIME_PACKAGE.ID.eq(id))
                .fetchOptional();
    }

    public Optional<TimePackageRecord> findByStudentPhoneAndType(String studentPhone, String type) {
        return dslContext.select(TIME_PACKAGE.fields()).from(TIME_PACKAGE)
                .innerJoin(STUDENT).on(STUDENT.ID.eq(TIME_PACKAGE.STUDENT_ID))
                .where(STUDENT.PHONE.eq(studentPhone))
                .and(TIME_PACKAGE.TYPE.eq(type))
                .fetchOptionalInto(TimePackageRecord.class);
    }

    public void updateAllTimeAmount(Long id, int amount, int initialAmount) {
        dslContext.update(TIME_PACKAGE)
                .set(TIME_PACKAGE.AMOUNT_IN_MINUTES, amount)
                .set(TIME_PACKAGE.INITIAL_AMOUNT_IN_MINUTES, initialAmount)
                .where(TIME_PACKAGE.ID.eq(id))
                .execute();
    }

    public List<TimePackageRecord> findAllByCourseId(Long courseId) {
        return dslContext.select(TIME_PACKAGE.fields()).from(TIME_PACKAGE)
                .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.STUDENT_ID.eq(TIME_PACKAGE.STUDENT_ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(STUDENT_COURSE.COURSE_ID))
                .where(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .and(TIME_PACKAGE.TYPE.eq(COURSE.TYPE))
                .fetchInto(TimePackageRecord.class);
    }

    public void batchUpdateAmount(List<TimePackageRecord> timePackageRecords) {
        var update = timePackageRecords.stream()
                .map(timePackageRecord ->
                        dslContext.update(TIME_PACKAGE)
                                .set(TIME_PACKAGE.AMOUNT_IN_MINUTES, timePackageRecord.getAmountInMinutes())
                                .where(TIME_PACKAGE.ID.eq(timePackageRecord.getId()))
                ).toList();
        dslContext.batch(update).execute();
    }

    public boolean existsByTypeAndStudentId(String type, Long studentId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(TIME_PACKAGE)
                        .where(TIME_PACKAGE.TYPE.eq(type))
                        .and(TIME_PACKAGE.STUDENT_ID.eq(studentId))
        );
    }

    @Override
    public Optional<TimePackageRecord> findByUserIdAndCourseId(Long userId, Long courseId) {
        return dslContext.select(TIME_PACKAGE.fields()).from(TIME_PACKAGE)
                .innerJoin(STUDENT).on(STUDENT.ID.eq(TIME_PACKAGE.STUDENT_ID))
                .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.STUDENT_ID.eq(TIME_PACKAGE.STUDENT_ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(STUDENT_COURSE.COURSE_ID)).and(COURSE.TYPE.eq(TIME_PACKAGE.TYPE))
                .where(STUDENT.USER_ID.eq(userId))
                .and(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .fetchOptionalInto(TimePackageRecord.class);
    }

    @Override
    public List<TimePackageRecord> findAllByStudentId(Long studentId) {
        return dslContext.selectFrom(TIME_PACKAGE)
                .where(TIME_PACKAGE.STUDENT_ID.eq(studentId))
                .fetchInto(TimePackageRecord.class);
    }

    @Override
    public List<CourseTimePackageResponse> findAllCourseTimePackageByStudentId(Long studentId) {
        return dslContext.select(TIME_PACKAGE.asterisk(), COURSE.asterisk()).from(TIME_PACKAGE)
                .innerJoin(STUDENT).on(STUDENT.ID.eq(TIME_PACKAGE.STUDENT_ID))
                .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.STUDENT_ID.eq(TIME_PACKAGE.STUDENT_ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(STUDENT_COURSE.COURSE_ID)).and(COURSE.TYPE.eq(TIME_PACKAGE.TYPE))
                .where(STUDENT.ID.eq(studentId))
                .fetch(courseTimePackageResponseRecordMapper);
    }
}
