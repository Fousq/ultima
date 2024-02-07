package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.currency.CurrencyService;
import com.example.rbapp.paymentrate.entity.PaymentRate;
import com.example.rbapp.paymentrate.service.PaymentRateService;
import com.example.rbapp.teacher.controller.api.TeacherInviteRequest;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherMailService;
import com.example.rbapp.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherInviteService {

    private final TeacherUserService teacherUserService;

    private final PaymentRateService paymentRateService;

    private final CurrencyService currencyService;

    private final BitrixService bitrixService;

    private final TeacherService teacherService;

    private final TeacherMailService teacherMailService;

    public void processInvite(TeacherInviteRequest request) {
        Teacher teacher = teacherUserService.create(request);
        List<PaymentRate> paymentRates = createPaymentRates(request);

        paymentRateService.create(paymentRates, teacher.getId());
        Long bitrixId = bitrixService.createTeacherUser(teacher);
        teacherService.updateBitrixId(teacher, bitrixId);
        teacherMailService.sendMail(teacher);
    }

    private List<PaymentRate> createPaymentRates(TeacherInviteRequest request) {
        Long currencyId = currencyService.getIdByCode(request.currency());

        PaymentRate individualPaymentRate = new PaymentRate();
        individualPaymentRate.setAmount(request.individualPrice());
        individualPaymentRate.setType("individual");
        individualPaymentRate.setCurrencyId(currencyId);

        PaymentRate pairPaymentRate = new PaymentRate();
        pairPaymentRate.setCurrencyId(currencyId);
        pairPaymentRate.setType("pair");
        pairPaymentRate.setAmount(request.pairPrice());

        PaymentRate groupPaymentRate = new PaymentRate();
        groupPaymentRate.setCurrencyId(currencyId);
        groupPaymentRate.setType("group");
        groupPaymentRate.setAmount(request.groupPrice());

        return List.of(individualPaymentRate, pairPaymentRate, groupPaymentRate);
    }
}
