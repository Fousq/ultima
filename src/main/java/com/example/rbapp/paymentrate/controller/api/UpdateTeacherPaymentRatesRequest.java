package com.example.rbapp.paymentrate.controller.api;

import java.util.List;

public record UpdateTeacherPaymentRatesRequest(Long teacherId,
                                               List<UpdatePaymentRateRequest> ratesToUpdate) {
}
