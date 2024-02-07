package com.example.rbapp.paymentrate.service.recordmapper;

import com.example.rbapp.paymentrate.job.model.TeacherPaymentReport;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.example.rbapp.jooq.codegen.Tables.CURRENCY;
import static com.example.rbapp.jooq.codegen.Tables.TEACHER;

@Service
public class TeacherPaymentReportRecordMapper implements RecordMapper<Record, TeacherPaymentReport> {

    @Override
    public TeacherPaymentReport map(Record record) {
        return TeacherPaymentReport.builder()
                .id(record.getValue(TEACHER.ID))
                .name(record.getValue(TEACHER.NAME))
                .surname(record.getValue(TEACHER.SURNAME))
                .total((BigDecimal) record.getValue("total"))
                .currencyCode(record.getValue(CURRENCY.CODE))
                .build();
    }
}
