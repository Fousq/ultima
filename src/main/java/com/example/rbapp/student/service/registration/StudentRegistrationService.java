package com.example.rbapp.student.service.registration;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.api.service.registration.RegistrationService;
import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.student.service.StudentMapper;
import com.example.rbapp.student.service.StudentRepository;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserMapper;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentRegistrationService implements RegistrationService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final BitrixService bitrixService;

    @Transactional
    @Override
    public void register(RegistrationRequest entity) {
        Long userId = createUser(entity);

        var student = studentMapper.mapToRecord(entity);
        student.setUserId(userId);
        Long studentId = studentRepository.create(student);
        if (bitrixService.isClientExists(student.getPhone())) {
            return;
        }
        Student studentEntity = studentMapper.mapToEntity(student);
        Long bitrixId = bitrixService.createClient(studentEntity);
        studentRepository.updateBitrixIdById(bitrixId, studentId);
    }

    private Long createUser(RegistrationRequest entity) {
        User user;
        if (StringUtils.isNotBlank(entity.username()) && StringUtils.isNotBlank(entity.password())) {
            user = userMapper.mapRegistrationRequestToEntity(entity);
        } else {
            user = new User(); // TODO move to mapper
            user.setUsername(entity.email());
            user.setPassword(entity.name() + entity.phone());
        }
        user.setGrantType("ROLE_STUDENT");
        return userService.create(user);
    }
}
