package com.example.rbapp.teacher.service.registartion;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.api.service.registration.RegistrationService;
import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherRepository;
import com.example.rbapp.teacher.service.TeacherMapper;
import com.example.rbapp.user.service.UserMapper;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherRegistrationService implements RegistrationService {

    private static final String GRANT_TYPE = "ROLE_TEACHER";

    private final UserService userService;
    private final TeacherRepository teacherRepository;
    private final UserMapper userMapper;
    private final TeacherMapper teacherMapper;
    private final BitrixService bitrixService;

    @Transactional
    @Override
    public void register(RegistrationRequest request) {
        var userId = createUser(request);

        var teacher = teacherMapper.mapRegistrationRequestToRecord(request, userId);
        Long teacherId = teacherRepository.create(teacher);
        Teacher teacherEntity = teacherMapper.mapRecordToEntity(teacher);
        Long bitrixId = bitrixService.createTeacherUser(teacherEntity);
        teacherRepository.updateBitrixIdById(bitrixId, teacherId);
    }

    private Long createUser(RegistrationRequest entity) {
        var user = userMapper.mapRegistrationRequestToEntity(entity, GRANT_TYPE);
        return userService.create(user);
    }
}
