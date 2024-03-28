package com.example.rbapp.paymentrate.service.recordmapper;

import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.example.rbapp.jooq.codegen.Tables.CURRENCY;
import static com.example.rbapp.jooq.codegen.Tables.PAYMENT_RATE;

@Component
public class PaymentRateResponseRecordMapper implements RecordMapper<Record, PaymentRateResponse> {

    @Override
    public PaymentRateResponse map(Record record) {
        return new PaymentRateResponse(
                record.getValue(PAYMENT_RATE.ID),
                record.getValue(PAYMENT_RATE.TYPE),
                record.getValue(PAYMENT_RATE.AMOUNT),
                record.getValue(CURRENCY.CODE)
        );
    }
}
