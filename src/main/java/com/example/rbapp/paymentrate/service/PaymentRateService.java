package com.example.rbapp.paymentrate.service;

import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.jooq.codegen.tables.records.PaymentRateRecord;
import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import com.example.rbapp.paymentrate.controller.api.UpdatePaymentRateRequest;
import com.example.rbapp.paymentrate.controller.api.UpdateTeacherPaymentRatesRequest;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import com.example.rbapp.paymentrate.model.PaymentReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.*;

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
        return paymentRateRepository.findAllActualByUserId(userId);
    }

    public List<Long> getTeacherIdsWithPaymentReports() {
        return paymentRateRepository.findTeacherIdsWithExistingPaymentRate();
    }

    public List<PaymentRateResponse> updateTeacherPaymentRates(UpdateTeacherPaymentRatesRequest request) {
        var paymentRateRecordsToUpdate = paymentRateRepository.findAllActualByTeacherId(request.teacherId()).stream()
                .filter(paymentRateRecord -> isNull(paymentRateRecord.getUpdateDate()))
                .map(paymentRateRecord -> request.ratesToUpdate().stream()
                        .filter(rateToUpdate -> rateToUpdate.type().equals(paymentRateRecord.getType()))
                        .findAny().map(rateToUpdate -> {
                            paymentRateRecord.setUpdateDate(LocalDateTime.now(DateTimeConstant.ALMATY));
                            return paymentRateRecord;
                        }).orElse(null)
                ).toList();
        paymentRateRepository.batchUpdate(paymentRateRecordsToUpdate);
        Long currencyId = paymentRateRecordsToUpdate.get(0).getCurrencyId();
        List<PaymentRateRecord> paymentRateRecordsToCreate =
                paymentRateMapper.mapUpdateRequestToRecord(request.ratesToUpdate(), request.teacherId(), currencyId);
        paymentRateRepository.batchCreate(paymentRateRecordsToCreate);
        return paymentRateMapper.mapRecordToResponse(
                paymentRateRepository.findAllActualByTeacherId(request.teacherId())
        );
    }

    public List<PaymentReport> getTeacherPaymentForCompletedLessonsReport(Long teacherId, Month month) {
        YearMonth yearMonth = Year.now().atMonth(month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return paymentRateRepository.findAllActualByTeacherId(teacherId).stream()
                .map(paymentRateRecord -> {
                    BigDecimal total =
                            paymentRateRepository.countTotalForPaymentRateOfCompletedLessonsBetween(
                                    paymentRateRecord.getId(),
                                    startDate,
                                    endDate
                            );
                    return new PaymentReport(paymentRateRecord.getType(), total, paymentRateRecord.getCurrencyId());
                })
                .toList();
    }

    public void putTeacherPaymentRates(UpdateTeacherPaymentRatesRequest request) {
        Map<String, UpdatePaymentRateRequest> map = request.ratesToUpdate().stream()
                .collect(toMap(UpdatePaymentRateRequest::type, Function.identity()));
        var paymentRateRecords = paymentRateRepository.findAllActualByTeacherId(request.teacherId()).stream()
                .map(paymentRateRecord -> {
                    if (map.containsKey(paymentRateRecord.getType())) {
                        paymentRateRecord.setAmount(map.get(paymentRateRecord.getType()).amount());
                    }
                    return paymentRateRecord;
                }).toList();
        paymentRateRepository.batchUpdateAmount(paymentRateRecords);
    }

    public List<PaymentReport> getTeacherPaymentForLessonsReport(Long teacherId, Month month) {
        YearMonth yearMonth = Year.now().atMonth(month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return paymentRateRepository.findAllActualByTeacherId(teacherId).stream()
                .map(paymentRateRecord -> {
                    BigDecimal total = paymentRateRepository.countTotalForPaymentRateOfLessonsBetween(
                            paymentRateRecord.getId(),
                            startDate,
                            endDate
                    );
                    return new PaymentReport(paymentRateRecord.getType(), total, paymentRateRecord.getCurrencyId());
                })
                .toList();
    }

    public List<PaymentRateResponse> getTeacherPaymentRateList(Long teacherId) {
        return paymentRateRepository.findAllActualByTeacherIdAsResponse(teacherId);
    }
}
