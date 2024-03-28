package com.example.rbapp.paymentrate.controller;

import com.example.rbapp.paymentrate.controller.api.PaymentMonthReportResponse;
import com.example.rbapp.paymentrate.controller.api.PaymentRateResponse;
import com.example.rbapp.paymentrate.controller.api.UpdateTeacherPaymentRatesRequest;
import com.example.rbapp.paymentrate.service.PaymentRateService;
import com.example.rbapp.paymentrate.service.PaymentReportService;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/payment/rate")
@RequiredArgsConstructor
public class PaymentRateController {

    private final PaymentReportService paymentReportService;
    private final PaymentRateService paymentRateService;
    private final UserService userService;

    @GetMapping("/teacher")
    public List<PaymentRateResponse> getTeacherPaymentRateList(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = userService.loadUserByToken(token).getId();
        return paymentRateService.getUserPaymentRateList(userId);
    }

    @GetMapping("/teacher/report/month/{monthId}")
    public PaymentMonthReportResponse getTeacherMonthReport(@RequestHeader(AUTHORIZATION) String token,
                                                            @PathVariable("monthId") Integer monthId) {
        return paymentReportService.getMonthReport(token, monthId);
    }

//    @PostMapping("/teacher")
//    public List<PaymentRateResponse> updateTeacherPaymentRates(@RequestBody UpdateTeacherPaymentRatesRequest request) {
//        return paymentRateService.updateTeacherPaymentRates(request);
//    }

    @PutMapping("/teacher")
    public void updateTeacherPaymentRates(@RequestBody UpdateTeacherPaymentRatesRequest request) {
        paymentRateService.putTeacherPaymentRates(request);
    }
}
