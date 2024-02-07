package com.example.rbapp.paymentrate.job;

import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.paymentrate.job.model.TeacherPaymentReport;
import com.example.rbapp.paymentrate.service.PaymentRateService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BitrixPaymentSendJob {

    private final JobScheduler jobScheduler;
    private final PaymentRateService paymentRateService;
    private final BitrixService bitrixService;

    @Value("${jobs.bitrix-teacher-payment.cron}")
    @Autowired
    private String cronExpression;

    @PostConstruct
    public void schedule() {
        jobScheduler.scheduleRecurrently(
                "bitrix-teacher-payment",
                cronExpression,
                this::execute
        );
    }

    public void execute() {
        // take yesterday to have a whole month to calculate payment for
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<TeacherPaymentReport> teacherPaymentReports = paymentRateService.getTeacherPaymentReports(yesterday);
        teacherPaymentReports.forEach(bitrixService::createPayment);
    }
}
