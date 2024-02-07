package com.example.rbapp.student.service;

import com.example.rbapp.jooq.codegen.tables.records.StudentRecord;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.student.controller.api.StudentUpdateRequest;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.config.AppMapperConfig;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface StudentMapper {

    StudentResponse mapToResponse(StudentRecord student);

    List<StudentResponse> mapToResponse(Collection<StudentRecord> student);

    StudentRecord mapToRecord(RegistrationRequest request);

    Student mapToEntity(StudentRecord studentRecord);

    List<Student> mapToEntity(List<StudentRecord> studentRecordList);

    StudentResponse mapEntityToResponse(Student student);

    StudentRecord mapUpdateRequestToRecord(StudentUpdateRequest request, Long id);

    StudentRecord mapEntityToRecord(Student student);
}
