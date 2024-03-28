package com.example.rbapp.paymentrate.service;

import com.example.rbapp.coursesubject.service.CourseSubjectService;
import com.example.rbapp.currency.CurrencyService;
import com.example.rbapp.paymentrate.controller.api.PaymentMonthReportResponse;
import com.example.rbapp.paymentrate.controller.api.PaymentReportResponse;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import com.example.rbapp.paymentrate.model.PaymentReport;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.teacher.service.TeacherService;
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
    private final TeacherService teacherService;
    private final CurrencyService currencyService;
    private final CourseSubjectService courseSubjectService;

    public PaymentMonthReportResponse getMonthReport(String token, Integer monthId) {
        User user = userService.loadUserByToken(token);
        Month month = Month.of(monthId);
        TeacherResponse teacher = teacherService.getByUserId(user.getId());
        List<PaymentReport> paymentReports = paymentRateService.getTeacherPaymentReport(teacher.id(), month);
        // determine currency
        String currencyCode = currencyService.getCodeById(paymentReports.get(0).currencyId());
        // get final reports per type
        var paymentReportResponses = paymentReports.stream().map(paymentReport -> {
            Integer subjectCount = courseSubjectService.countSubjectsByCourseTypeForTeacher(teacher.id(), paymentReport.type());
            return new PaymentReportResponse(paymentReport.total(), paymentReport.type(), subjectCount);
        }).toList();
        return new PaymentMonthReportResponse(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH), currencyCode,
                paymentReportResponses);
    }
}
