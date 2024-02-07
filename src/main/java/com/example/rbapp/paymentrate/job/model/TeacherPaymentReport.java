package com.example.rbapp.paymentrate.job.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TeacherPaymentReport {

    Long id;
    String name;
    String surname;
    BigDecimal total;
    String currencyCode;
}
