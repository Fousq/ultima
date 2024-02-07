package com.example.rbapp.paymentrate.service;

import com.example.rbapp.paymentrate.controller.api.PaymentMonthReportResponse;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaymentReportService {

    private final UserService userService;
    private final PaymentRateService paymentRateService;

    public PaymentMonthReportResponse getMonthReport(String token, Integer monthId) {
        User user = userService.loadUserByToken(token);
        Month month = Month.of(monthId);
        BigDecimal total = paymentRateService.getTotalAmountByUserIdForMonth(user.getId(), month);
        return new PaymentMonthReportResponse(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH), total);
    }
}
