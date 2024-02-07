package com.example.rbapp.supervisor.service;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.api.service.registration.RegistrationService;
import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.head.teacher.service.HeadTeacherMapper;
import com.example.rbapp.head.teacher.service.HeadTeacherRepository;
import com.example.rbapp.jooq.codegen.tables.records.HeadTeacherRecord;
import com.example.rbapp.supervisor.entity.Supervisor;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserMapper;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupervisorRegistrationService implements RegistrationService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final SupervisorService supervisorService;
    private final SupervisorMapper supervisorMapper;
    private final HeadTeacherRepository headTeacherRepository;
    private final HeadTeacherMapper headTeacherMapper;
    private final BitrixService bitrixService;

    @Override
    public void register(RegistrationRequest entity) {
        Long userId = createUser(entity);

        Supervisor supervisor = supervisorMapper.mapRegistrationToEntity(entity);
        supervisor.setUserId(userId);
        Long supervisorId = supervisorService.create(supervisor);
        Long bitrixId = bitrixService.createSupervisor(supervisor);
        supervisorService.updateBitrixContact(bitrixId, supervisorId);

        // duplicate supervisor as head teacher per Front request. TODO delete it later
        createHeadTeacher(entity, userId, bitrixId);
    }

    private Long createUser(RegistrationRequest entity) {
        User user = userMapper.mapRegistrationRequestToEntity(entity);
        user.setGrantType("ROLE_SUPERVISOR");
        return userService.create(user);
    }

    private void createHeadTeacher(RegistrationRequest entity, Long userId, Long bitrixId) {
        HeadTeacherRecord headTeacher = headTeacherMapper.mapToRecord(entity);
        headTeacher.setUserId(userId);
        Long headTeacherId = headTeacherRepository.create(headTeacher);
        headTeacherRepository.updateBitrixIdById(bitrixId, headTeacherId);
    }
}
