package com.example.rbapp.head.teacher.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.head.teacher.controller.api.HeadTeacherResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.jooq.codegen.tables.records.HeadTeacherRecord;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface HeadTeacherMapper {

    HeadTeacherRecord mapToRecord(RegistrationRequest entity);

    List<HeadTeacherResponse> mapToResponse(Collection<HeadTeacherRecord> headTeacherList);

    HeadTeacherResponse mapToResponse(HeadTeacherRecord headTeacher);

    HeadTeacherRecord mapEntityToRecord(HeadTeacher headTeacher);

    HeadTeacher mapRecordToEntity(HeadTeacherRecord headTeacherRecord);
}
