package com.example.rbapp.studentuser;

import com.example.rbapp.student.controller.api.StudentApplicationRequest;
import com.example.rbapp.student.controller.api.StudentInviteRequest;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.student.service.StudentService;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentUserService {

    private final StudentService studentService;
    private final UserService userService;

    @Transactional
    public Student createUserStudent(StudentInviteRequest request) {
        Long userId = createUser(request.email(), request.phone());

        Student student = new Student();
        student.setName(request.name());
        student.setSurname(request.surname());
        student.setMiddleName(request.middleName());
        student.setPhone(request.phone());
        student.setEmail(request.email());
        student.setBirthday(request.birthday());
        student.setUserId(userId);
        Long id = studentService.create(student);

        return studentService.getStudent(id);
    }

    private Long createUser(String email, String phone) {
        User user = new User();
        user.setUsername(email);
        user.setPassword(phone);
        user.setGrantType("ROLE_STUDENT");
        return userService.create(user);
    }
}
