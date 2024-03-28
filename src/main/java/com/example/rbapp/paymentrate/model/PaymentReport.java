package com.example.rbapp.paymentrate.model;

import java.math.BigDecimal;

public record PaymentReport(String type, BigDecimal total, Long currencyId) {
}
