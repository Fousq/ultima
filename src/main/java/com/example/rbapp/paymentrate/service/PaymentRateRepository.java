package com.example.rbapp.paymentrate.service;

import com.example.rbapp.jooq.codegen.tables.records.PaymentRateRecord;
import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import com.example.rbapp.paymentrate.service.recordmapper.PaymentRateResponseRecordMapper;
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
    private final PaymentRateResponseRecordMapper paymentRateResponseRecordMapper;

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

    public List<PaymentRateRecord> findAllActualByTeacherId(Long teacherId) {
        return dslContext.selectFrom(PAYMENT_RATE)
                .where(PAYMENT_RATE.TEACHER_ID.eq(teacherId))
                .and(PAYMENT_RATE.UPDATE_DATE.isNull())
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

    public List<PaymentRateResponse> findAllActualByUserId(Long userId) {
        return dslContext.select(PAYMENT_RATE.asterisk(), CURRENCY.CODE).from(PAYMENT_RATE)
                .innerJoin(TEACHER).on(TEACHER.ID.eq(PAYMENT_RATE.TEACHER_ID))
                .innerJoin(CURRENCY).on(CURRENCY.ID.eq(PAYMENT_RATE.CURRENCY_ID))
                .where(TEACHER.USER_ID.eq(userId))
                .and(PAYMENT_RATE.UPDATE_DATE.isNull())
                .fetch(paymentRateResponseRecordMapper);
    }

    public List<PaymentRateRecord> findTeacherPaymentRateBetween(LocalDate startDate, LocalDate endDate, Long teacherId) {
        return dslContext.select(PAYMENT_RATE.asterisk())
                .from(PAYMENT_RATE)
                .innerJoin(TEACHER).on(TEACHER.ID.eq(PAYMENT_RATE.TEACHER_ID))
                .where(PAYMENT_RATE.UPDATE_DATE.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                .and(PAYMENT_RATE.TEACHER_ID.eq(teacherId))
                .orderBy(PAYMENT_RATE.UPDATE_DATE)
                .fetchInto(PaymentRateRecord.class);
    }

    public BigDecimal countTotalForPaymentRateBetween(Long paymentRateId, LocalDate startDate, LocalDate endDate) {
        return dslContext.select(sum(PAYMENT_RATE.AMOUNT))
                .from(PAYMENT_RATE)
                .innerJoin(TEACHER).on(TEACHER.ID.eq(PAYMENT_RATE.TEACHER_ID))
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.TEACHER_ID.eq(TEACHER.ID))
                .innerJoin(COURSE).on(COURSE.ID.eq(TEACHER_COURSE.COURSE_ID)).and(COURSE.TYPE.eq(PAYMENT_RATE.TYPE))
                .innerJoin(COURSE_SUBJECT).on(COURSE_SUBJECT.COURSE_ID.eq(COURSE.ID))
                .where(COURSE_SUBJECT.START_AT.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                .and(PAYMENT_RATE.ID.eq(paymentRateId))
                .fetchSingleInto(BigDecimal.class);
    }

    public void batchUpdate(List<PaymentRateRecord> paymentRateRecords) {
        var update = paymentRateRecords.stream().map(paymentRateRecord -> dslContext.update(PAYMENT_RATE)
                .set(PAYMENT_RATE.AMOUNT, paymentRateRecord.getAmount())
                .set(PAYMENT_RATE.UPDATE_DATE, paymentRateRecord.getUpdateDate())
        ).toList();
        dslContext.batch(update).execute();
    }

    public List<Long> findTeacherIdsWithExistingPaymentRate() {
        return dslContext.selectDistinct(PAYMENT_RATE.TEACHER_ID).from(PAYMENT_RATE).fetchInto(Long.class);
    }
}
