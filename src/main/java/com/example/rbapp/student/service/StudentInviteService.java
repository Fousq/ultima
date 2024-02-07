package com.example.rbapp.student.service;

import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.student.controller.api.StudentInviteRequest;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.studentuser.StudentUserService;
import com.example.rbapp.timepackage.entity.TimePackage;
import com.example.rbapp.timepackage.service.TimePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInviteService {

    private final StudentUserService studentUserService;

    private final TimePackageService timePackageService;

    private final BitrixService bitrixService;

    private final StudentService studentService;

    private final InviteStudentMailService inviteStudentMailService;

    public void processInvitation(StudentInviteRequest request) {
        // TODO merge student and timePackage into one method to wrap in transaction
        Student student = studentUserService.createUserStudent(request);

        TimePackage timePackage = timePackageService.createStudentPackage(request.email(), request.timePackageType(),
                request.timePackageAmount());

        Long bitrixId = bitrixService.createClient(student, timePackage);
        studentService.updateBitrixId(student, bitrixId);
        inviteStudentMailService.sendEmail(student);
    }
}
