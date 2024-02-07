package com.example.rbapp.paymentrate.controller.api;

import java.math.BigDecimal;

public record PaymentMonthReportResponse(String month, BigDecimal total) {
}
