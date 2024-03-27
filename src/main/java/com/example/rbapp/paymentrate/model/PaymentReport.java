package com.example.rbapp.paymentrate.model;

import java.math.BigDecimal;

public record PaymentReport(BigDecimal total, Long currencyId) {
}
