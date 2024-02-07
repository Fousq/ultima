package com.example.rbapp.studentcourse.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.course.controller.api.CourseStudentResponse;
import com.example.rbapp.jooq.codegen.tables.records.StudentCourseRecord;
import com.example.rbapp.student.controller.api.StudentCourseRequest;
import com.example.rbapp.studentcourse.entity.StudentCourse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface StudentCourseMapper {
    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "surname", source = "student.surname")
    @Mapping(target = "email", source = "student.email")
    @Mapping(target = "phone", source = "student.phone")
    CourseStudentResponse mapToResponse(StudentCourse studentCourse);

    List<CourseStudentResponse> mapToResponse(Collection<CourseStudentResponse> courseStudentRespons);

    @Mapping(target = "studentId", source = "studentCourseRequest.id")
    @Mapping(target = "courseId", source = "courseId")
    StudentCourseRecord mapToRecord(StudentCourseRequest studentCourseRequest, Long courseId);

    List<StudentCourseRecord> mapToRecord(List<StudentCourseRequest> studentCourseRequest, @Context Long courseId);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    StudentCourseRecord mapToRecord(StudentCourse studentCourse);

    List<StudentCourseRecord> mapToRecord(List<StudentCourse> studentCourseList);

    default StudentCourseRecord mapStudentCourseRecordContext(StudentCourseRequest studentCourseRequest, @Context Long courseId) {
        return mapToRecord(studentCourseRequest, courseId);
    }
}
