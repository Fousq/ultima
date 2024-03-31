package com.example.rbapp.teacher.service;

import com.example.rbapp.course.service.CourseResponseMapper;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.teacher.controller.api.TeacherSaveRequest;
import com.example.rbapp.teacher.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class, uses = CourseResponseMapper.class)
public interface TeacherMapper {
    @Mapping(target = "isPayableForCanceledLesson", source = "payableForCanceledLesson")
    TeacherResponse mapRecordToResponse(TeacherRecord teacher);

    @Mapping(target = "inCourses", source = "courses")
    @Mapping(target = "isPayableForCanceledLesson", source = "teacher.payableForCanceledLesson")
    TeacherResponse mapRecordToResponse(TeacherRecord teacher, Collection<CourseRecord> courses);

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

    @Mapping(target = "isPayableForCanceledLesson", source = "payableForCanceledLesson")
    Teacher mapRecordToEntity(TeacherRecord teacherRecord);

    TeacherRecord mapSaveRequestToRecord(TeacherSaveRequest teacherSaveRequest, Long id);

    @Mapping(target = "payableForCanceledLesson", source = "isPayableForCanceledLesson", defaultValue = "false")
    TeacherRecord mapEntityToRecord(Teacher teacher);
}
