package com.example.rbapp.paymentrate.controller.api;

import java.math.BigDecimal;
import java.util.List;

public record PaymentMonthReportResponse(String month,
                                         String currencyCode,
                                         List<PaymentReportResponse> paymentReportResponses) {
}
