package com.example.rbapp.teacher.service;

import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.teacher.controller.api.TeacherSaveRequest;
import com.example.rbapp.teacher.entity.Teacher;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface TeacherMapper {
    TeacherResponse mapRecordToResponse(TeacherRecord teacher);

    List<TeacherResponse> mapEntityToResponse(Collection<TeacherRecord> teachers);

    TeacherResponse mapEntityToResponse(Teacher teacher);

    List<TeacherResponse> mapEntityToResponse(List<Teacher> teacherList);

    TeacherRecord mapRegistrationRequestToRecord(RegistrationRequest request);

    default TeacherRecord mapRegistrationRequestToRecord(RegistrationRequest request, Long userId) {
        TeacherRecord teacherRecord = mapRegistrationRequestToRecord(request);
        teacherRecord.setUserId(userId);
        return teacherRecord;
    }

    List<Teacher> mapRecordToEntity(List<TeacherRecord> teacherRecordList);

    Teacher mapRecordToEntity(TeacherRecord teacherRecord);

    TeacherRecord mapSaveRequestToRecord(TeacherSaveRequest teacherSaveRequest, Long id);

    TeacherRecord mapEntityToRecord(Teacher teacher);
}
