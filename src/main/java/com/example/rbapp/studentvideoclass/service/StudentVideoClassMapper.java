package com.example.rbapp.studentvideoclass.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.StudentCourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentVideoClassRecord;
import com.example.rbapp.studentvideoclass.api.StudentVideoClassResponse;
import com.example.rbapp.studentvideoclass.api.StudentVideoClassUpdateRequest;
import com.example.rbapp.studentvideoclass.entity.StudentVideoClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface StudentVideoClassMapper {
    StudentVideoClass mapToEntity(StudentVideoClassRecord studentVideoClassRecord);

    List<StudentVideoClass> mapToEntity(Collection<StudentVideoClassRecord> studentVideoClassRecords);

    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "surname", source = "student.surname")
    StudentVideoClassResponse mapToResponse(StudentVideoClass studentVideoClass);

    List<StudentVideoClassResponse> mapToResponse(Collection<StudentVideoClass> studentVideoClassList);

    @Mapping(target = "studentId", source = "student.id")
    StudentVideoClassRecord mapToRecord(StudentVideoClass studentVideoClass);

    List<StudentVideoClassRecord> mapToRecord(List<StudentVideoClass> studentVideoClassList);

    @Mapping(target = "studentId", source = "id")
    @Mapping(target = "visited", source = "visited", defaultValue = "false")
    StudentVideoClassRecord mapUpdateRequestToRecord(StudentVideoClassUpdateRequest studentVideoClassUpdateRequest);

    List<StudentVideoClassRecord> mapUpdateRequestToRecord(
            Collection<StudentVideoClassUpdateRequest> studentVideoClassUpdateRequests
    );
}
