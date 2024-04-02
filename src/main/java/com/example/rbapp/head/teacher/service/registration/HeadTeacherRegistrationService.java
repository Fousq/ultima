package com.example.rbapp.head.teacher.service.registration;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.api.service.registration.RegistrationService;
import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.jooq.codegen.tables.records.HeadTeacherRecord;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.head.teacher.service.HeadTeacherMapper;
import com.example.rbapp.user.service.UserMapper;
import com.example.rbapp.head.teacher.service.HeadTeacherRepository;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeadTeacherRegistrationService implements RegistrationService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final HeadTeacherRepository headTeacherRepository;
    private final HeadTeacherMapper headTeacherMapper;
    private final BitrixService bitrixService;

    @Override
    public void register(RegistrationRequest entity) {
        Long userId = createUser(entity);

        HeadTeacherRecord headTeacher = headTeacherMapper.mapToRecord(entity);
        headTeacher.setUserId(userId);
        Long headTeacherId = headTeacherRepository.create(headTeacher);
        HeadTeacher headTeacherEntity = headTeacherMapper.mapRecordToEntity(headTeacher);
        Long bitrixId = bitrixService.createHeadTeacherContact(headTeacherEntity);
        headTeacherRepository.updateBitrixIdById(bitrixId, headTeacherId);
    }

    private Long createUser(RegistrationRequest entity) {
        User user = userMapper.mapRegistrationRequestToEntity(entity);
        user.setGrantType("ROLE_HEAD_TEACHER");
        return userService.create(user);
    }
}
