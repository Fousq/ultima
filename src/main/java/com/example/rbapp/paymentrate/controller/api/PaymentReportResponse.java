package com.example.rbapp.paymentrate.controller.api;

import java.math.BigDecimal;

public record PaymentReportResponse(BigDecimal total, String type, Integer subjectsCount) {
}
