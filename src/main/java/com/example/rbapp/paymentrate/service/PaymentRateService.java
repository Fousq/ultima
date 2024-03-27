package com.example.rbapp.paymentrate.service;

import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.jooq.codegen.tables.records.PaymentRateRecord;
import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import com.example.rbapp.paymentrate.controller.api.UpdateTeacherPaymentRatesRequest;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import com.example.rbapp.paymentrate.model.PaymentReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

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
        List<PaymentRateRecord> paymentRateRecords = paymentRateRepository.findAllActualByUserId(userId);
        return paymentRateMapper.mapRecordToResponse(paymentRateRecords);
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

    public PaymentReport getTeacherPaymentReport(Long teacherId, LocalDate date) {
        LocalDate endDate = date;
        LocalDate startDate = date.withDayOfMonth(1);

        var paymentRates = paymentRateRepository.findTeacherPaymentRateBetween(startDate, endDate, teacherId);
        paymentRates.addAll(paymentRateRepository.findAllActualByTeacherId(teacherId));
        var paymentRatesByType = paymentRates.stream()
                .collect(Collectors.groupingBy(PaymentRateRecord::getType, mapping(Function.identity(), toList())));
        BigDecimal total = paymentRatesByType.values().stream()
                .map(paymentRateRecords -> calculateTotal(paymentRateRecords, startDate, endDate))
                .reduce(new BigDecimal("0.00"), BigDecimal::add);

        PaymentRateRecord actualPaymentRate = paymentRates.get(paymentRates.size() - 1);
        return new PaymentReport(total, actualPaymentRate.getCurrencyId());
    }

    private BigDecimal calculateTotal(List<PaymentRateRecord> paymentRates, LocalDate startDate, LocalDate endDate) {
        LocalDate paymentStart = startDate;
        BigDecimal total = new BigDecimal("0.00");
        for (PaymentRateRecord paymentRate : paymentRates) {
            LocalDate paymentTill;
            if (nonNull(paymentRate.getUpdateDate())) {
                paymentTill = paymentRate.getUpdateDate().toLocalDate();
            } else {
                paymentTill = endDate;
            }
            total = total.add(
                    paymentRateRepository.countTotalForPaymentRateBetween(paymentRate.getId(), paymentStart, paymentTill)
            );
            paymentStart = paymentTill;
        }
        return total;
    }
}
