package com.example.rbapp.paymentrate.service;

import com.example.rbapp.jooq.codegen.tables.records.PaymentRateRecord;
import com.example.rbapp.paymentrate.job.model.TeacherPaymentReport;
import com.example.rbapp.paymentrate.service.recordmapper.TeacherPaymentReportRecordMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.rbapp.jooq.codegen.Tables.*;
import static org.jooq.impl.DSL.sum;

@Repository
@RequiredArgsConstructor
public class PaymentRateRepository {

    private final DSLContext dslContext;
    private final TeacherPaymentReportRecordMapper teacherPaymentReportRecordMapper;

    public void batchCreate(List<PaymentRateRecord> paymentRateRecords) {
        var insert = paymentRateRecords.stream()
                .map(paymentRateRecord -> dslContext.insertInto(PAYMENT_RATE)
                        .set(PAYMENT_RATE.TYPE, paymentRateRecord.getType())
                        .set(PAYMENT_RATE.CURRENCY_ID, paymentRateRecord.getCurrencyId())
                        .set(PAYMENT_RATE.AMOUNT, paymentRateRecord.getAmount())
                        .set(PAYMENT_RATE.TEACHER_ID, paymentRateRecord.getTeacherId()))
                .toList();
        dslContext.batch(insert).execute();
    }

    public List<PaymentRateRecord> findAllByTeacherId(Long teacherId) {
        return dslContext.selectFrom(PAYMENT_RATE)
                .where(PAYMENT_RATE.TEACHER_ID.eq(teacherId))
                .fetchInto(PaymentRateRecord.class);
    }

    public BigDecimal calculateTotalAmountByUserIdBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return dslContext.select(sum(PAYMENT_RATE.AMOUNT)).from(PAYMENT_RATE)
                .innerJoin(TEACHER).on(TEACHER.ID.eq(PAYMENT_RATE.TEACHER_ID))
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.TEACHER_ID.eq(TEACHER.ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(TEACHER_COURSE.COURSE_ID))
                .innerJoin(COURSE_SUBJECT).on(COURSE_SUBJECT.COURSE_ID.eq(COURSE.ID))
                .where(TEACHER.USER_ID.eq(userId))
                .and(COURSE_SUBJECT.START_AT.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                .fetchSingleInto(BigDecimal.class);
    }

    public List<PaymentRateRecord> findAllByUserId(Long userId) {
        return dslContext.select(PAYMENT_RATE.fields()).from(PAYMENT_RATE)
                .innerJoin(TEACHER).on(TEACHER.ID.eq(PAYMENT_RATE.TEACHER_ID))
                .where(TEACHER.USER_ID.eq(userId))
                .fetchInto(PaymentRateRecord.class);
    }

    public List<TeacherPaymentReport> findTeacherPaymentReportBetween(LocalDate startDate, LocalDate endDate) {
        return dslContext.select(TEACHER.ID, TEACHER.NAME, TEACHER.SURNAME, sum(PAYMENT_RATE.AMOUNT).as("total"), CURRENCY.CODE)
                .from(PAYMENT_RATE)
                .innerJoin(CURRENCY).on(CURRENCY.ID.eq(PAYMENT_RATE.CURRENCY_ID))
                .innerJoin(TEACHER).on(TEACHER.ID.eq(PAYMENT_RATE.TEACHER_ID))
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.TEACHER_ID.eq(TEACHER.ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(TEACHER_COURSE.COURSE_ID))
                .innerJoin(COURSE_SUBJECT).on(COURSE_SUBJECT.COURSE_ID.eq(COURSE.ID))
                .where(COURSE_SUBJECT.START_AT.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                .groupBy(TEACHER.ID, CURRENCY.CODE)
                .fetch(teacherPaymentReportRecordMapper);
    }
}
