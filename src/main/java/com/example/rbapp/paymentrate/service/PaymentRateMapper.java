package com.example.rbapp.paymentrate.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.PaymentRateRecord;
import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface PaymentRateMapper {
    List<PaymentRate> mapRecordToEntity(List<PaymentRateRecord> paymentRateRecords);

    PaymentRate mapRecordToEntity(PaymentRateRecord paymentRateRecord);

    List<PaymentRateRecord> mapEntityToRecord(List<PaymentRate> paymentRates, @Context Long teacherId);

    @Mapping(target = "teacherId", source = "teacherId")
    PaymentRateRecord mapEntityToRecord(PaymentRate paymentRate, Long teacherId);

    default PaymentRateRecord mapEntityToRecordContext(PaymentRate paymentRate, @Context Long teacherId) {
        return mapEntityToRecord(paymentRate, teacherId);
    }

    List<PaymentRateResponse> mapRecordToResponse(List<PaymentRateRecord> paymentRateRecords);

    PaymentRateResponse mapRecordToResponse(PaymentRateRecord paymentRateRecord);
}
