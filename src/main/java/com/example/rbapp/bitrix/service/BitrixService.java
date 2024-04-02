package com.example.rbapp.bitrix.service;

import com.example.rbapp.bitrix.api.*;
import com.example.rbapp.bitrix.client.BitrixClient;
import com.example.rbapp.bitrix.service.model.BookPurchaseApplication;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.paymentrate.job.model.TeacherPaymentReport;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.supervisor.entity.Supervisor;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.timepackage.entity.TimePackage;
import com.example.rbapp.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BitrixService {

    private static final String WORK_VALUE_TYPE = "WORK";
    private static final String FROM_SITE = "https://ultima.deutschkz.online";
    private static final Long DEAL_CATEGORY_ID = 5L;

    private final BitrixClient bitrixClient;

    public Long createClient(Student student) {
        String title = student.getName() + student.getPhone();
        var phone = List.of(new BitrixValue(student.getPhone(), WORK_VALUE_TYPE));
        var email = List.of(new BitrixValue(student.getEmail(), WORK_VALUE_TYPE));
        var clientFields = new BitrixCreateClientFields(title, student.getName(), email, phone);
        CreateClientRequest createClientRequest = new CreateClientRequest(clientFields);
        return bitrixClient.createClient(createClientRequest).clientId();
    }

    public Long createClient(Student student, TimePackage timePackage) {
        Long clientId = createClient(student);
        var dealFields = new BitrixCreateDealFields(FROM_SITE, DEAL_CATEGORY_ID, clientId);
        CreateDealRequest createDealRequest = new CreateDealRequest(dealFields);
        bitrixClient.createDeal(createDealRequest);
        return clientId;
    }

    public Long createTeacherUser(Teacher teacher) {
        return bitrixClient.createUser(new CreateUserRequest(55L,
                teacher.getEmail(),
                teacher.getName(),
                teacher.getSurname(),
                "Учитель",
                teacher.getPhone())).id();
    }

    public Long createHeadTeacherUser(HeadTeacher headTeacher) {
        return bitrixClient.createUser(new CreateUserRequest(53L,
                headTeacher.getEmail(),
                headTeacher.getName(),
                headTeacher.getSurname(),
                "Куратор",
                headTeacher.getPhone()))
                .id();
    }

    public void createInvitation(Teacher teacher) {
        log.info("Create invitation");
        //bitrixClient.createInvitation(new InvitationRequest());
    }

    public void createClientApplication(Student student) {
        var fields = new BitrixCreateDealFields(FROM_SITE, DEAL_CATEGORY_ID, student.getBitrixContactId());
        var request = new CreateDealRequest(fields);
        bitrixClient.createDeal(request);
    }

    public void createPayment(TeacherPaymentReport teacherPaymentReport) {
        String fullName = teacherPaymentReport.getName() + " " + teacherPaymentReport.getSurname();
        String title = fullName + " - " + teacherPaymentReport.getTotal().toPlainString();

        BitrixCreatePaymentCardFields bitrixCreatePaymentCardFields = new BitrixCreatePaymentCardFields(
                title,
                11L,
                teacherPaymentReport.getCurrencyCode(),
                teacherPaymentReport.getTotal(),
                teacherPaymentReport.getTotal(),
                fullName
        );
        var request = new CreatePaymentCardRequest(146L, bitrixCreatePaymentCardFields);
        bitrixClient.createPaymentCard(request);
    }

    public Boolean isClientExists(String phone) {
        ClientListResponse response = bitrixClient.getClientList(new ClientListRequest(phone));
        return response.total() > 0;
    }

    public Long createSupervisor(Supervisor supervisor) {
        return bitrixClient.createUser(new CreateUserRequest(53L,
                        supervisor.getEmail(),
                        supervisor.getName(),
                        supervisor.getSurname(),
                        "Руководитель",
                        supervisor.getPhone()))
                .id();
    }

    public void createBookPurchaseApplication(BookPurchaseApplication bookPurchaseApplication) {
        // TODO uncomment after Bitrix implementation
//        bitrixClient.createBookPurchaseApplication(new CreateBookPurchaseApplicationRequest());
    }

    public Long createTeacherContact(Teacher teacher) {
        String title = teacher.getName() + teacher.getPhone();
        var phone = List.of(new BitrixValue(teacher.getPhone(), WORK_VALUE_TYPE));
        var email = List.of(new BitrixValue(teacher.getEmail(), WORK_VALUE_TYPE));
        // 2 - преподаватель
        var clientFields = new BitrixCreateContactWithTypeFields(title, 2, teacher.getName(), email, phone);
        var createContactWithTypeRequest = new CreateContactWithTypeRequest(clientFields);
        return bitrixClient.createContactWithType(createContactWithTypeRequest).clientId();
    }

    public Long createHeadTeacherContact(HeadTeacher headTeacher) {
        String title = headTeacher.getName() + headTeacher.getPhone();
        var phone = List.of(new BitrixValue(headTeacher.getPhone(), WORK_VALUE_TYPE));
        var email = List.of(new BitrixValue(headTeacher.getEmail(), WORK_VALUE_TYPE));
        // 1 - куратор
        var clientFields = new BitrixCreateContactWithTypeFields(title, 1, headTeacher.getName(), email, phone);
        var createContactWithTypeRequest = new CreateContactWithTypeRequest(clientFields);
        return bitrixClient.createContactWithType(createContactWithTypeRequest).clientId();
    }
}
