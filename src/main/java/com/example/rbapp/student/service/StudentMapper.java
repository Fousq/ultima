package com.example.rbapp.student.service;

import com.example.rbapp.course.service.CourseResponseMapper;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentRecord;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.student.controller.api.StudentUpdateRequest;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.config.AppMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class, uses = {CourseResponseMapper.class})
public interface StudentMapper {

    StudentResponse mapToResponse(StudentRecord student);

    @Mapping(target = "inCourses", source = "courses")
    StudentResponse mapToResponse(StudentRecord student, List<CourseRecord> courses);

    List<StudentResponse> mapToResponse(Collection<StudentRecord> student);

    StudentRecord mapToRecord(RegistrationRequest request);

    Student mapToEntity(StudentRecord studentRecord);

    List<Student> mapToEntity(List<StudentRecord> studentRecordList);

    StudentResponse mapEntityToResponse(Student student);

    StudentRecord mapUpdateRequestToRecord(StudentUpdateRequest request, Long id);

    StudentRecord mapEntityToRecord(Student student);
}
