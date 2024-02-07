package com.example.rbapp.paymentrate.service;

import com.example.rbapp.jooq.codegen.tables.records.PaymentRateRecord;
import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import com.example.rbapp.paymentrate.job.model.TeacherPaymentReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentRateService {

    private final PaymentRateRepository paymentRateRepository;
    private final PaymentRateMapper paymentRateMapper;

    public void create(List<PaymentRate> paymentRates, Long teacherId) {
        List<PaymentRateRecord> paymentRateRecords = paymentRateMapper.mapEntityToRecord(paymentRates, teacherId);
        paymentRateRepository.batchCreate(paymentRateRecords);
    }

    public BigDecimal getTotalAmountByUserIdForMonth(Long userId, Month month) {
        YearMonth yearMonth = Year.now().atMonth(month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return Optional.ofNullable(paymentRateRepository.calculateTotalAmountByUserIdBetween(userId, startDate, endDate))
                .orElse(BigDecimal.ZERO);
    }

    public List<PaymentRateResponse> getUserPaymentRateList(Long userId) {
        List<PaymentRateRecord> paymentRateRecords = paymentRateRepository.findAllByUserId(userId);
        return paymentRateMapper.mapRecordToResponse(paymentRateRecords);
    }

    public List<TeacherPaymentReport> getTeacherPaymentReports(LocalDate date) {
        LocalDate endDate = date;
        LocalDate startDate = date.withDayOfMonth(1);

        return paymentRateRepository.findTeacherPaymentReportBetween(startDate, endDate);
    }
}
