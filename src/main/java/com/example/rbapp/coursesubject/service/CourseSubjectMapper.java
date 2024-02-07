package com.example.rbapp.coursesubject.service;

import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.coursesubject.controller.api.CourseSubjectPatchResponse;
import com.example.rbapp.coursesubject.controller.api.CourseSubjectResponse;
import com.example.rbapp.coursesubject.controller.api.CourseSubjectUpdateRequest;
import com.example.rbapp.coursesubject.controller.api.RecentCourseSubjectResponse;
import com.example.rbapp.coursesubject.entity.CourseSubject;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.homework.service.HomeworkMapper;
import com.example.rbapp.jooq.codegen.tables.records.CourseSubjectRecord;
import com.example.rbapp.student.service.StudentMapper;
import org.mapstruct.*;

import java.time.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

@Mapper(config = AppMapperConfig.class, uses = {HomeworkMapper.class, StudentMapper.class})
public interface CourseSubjectMapper {


    List<CourseSubject> mapToEntity(List<CourseSubjectRecord> courseSubjectRecords);

    @Mapping(target = "homework", ignore = true)
    @Mapping(target = "studentList", ignore = true)
    CourseSubject mapRecordToEntity(CourseSubjectRecord courseSubjectRecord);

    @Mapping(target = "courseId", source = "courseId")
    CourseSubjectRecord mapToRecord(CourseSubject courseSubject, Long courseId);

    List<CourseSubjectRecord> mapToRecord(List<CourseSubject> courseSubjectList, @Context Long courseId);

    default List<String> mapToStringList(String[] strings) {
        return strings != null ? Arrays.asList(strings) : List.of();
    }

    default CourseSubjectRecord mapCourseSubjectRecordContext(CourseSubject courseSubject, @Context Long courseId) {
        return mapToRecord(courseSubject, courseId);
    }

    @Mapping(target = "homeworkTitle", source = "homework.title")
    @Mapping(target = "homeworkDescription", source = "homework.description")
    @Mapping(target = "homeworkFiles", source = "homework.files")
    @Mapping(target = "homeworkList", source = "studentHomeworkList")
    CourseSubjectResponse mapToResponse(CourseSubject courseSubject);

    @Mapping(target = "homework.title", source = "homeworkTitle")
    @Mapping(target = "homework.description", source = "homeworkDescription")
    @Mapping(target = "homework.files", source = "homeworkFiles")
    @Mapping(target = "studentHomeworkList", source = "homeworkList")
    CourseSubject mapUpdateRequestToEntity(CourseSubjectUpdateRequest courseSubjectUpdateRequest);

    List<CourseSubject> mapUpdateRequestToEntity(Collection<CourseSubjectUpdateRequest> courseSubjectUpdateRequests);

    CourseSubjectPatchResponse mapEntityToPatchResponse(CourseSubject courseSubject);

    default LocalDateTime mapToLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    default ZonedDateTime mapLocalDateTimeToZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(DateTimeConstant.ALMATY);
    }

    default ZonedDateTime mapInstantToZonedDateTime(Instant instant) {
        return instant.atZone(DateTimeConstant.ALMATY);
    }

    RecentCourseSubjectResponse mapRecordToRecentResponse(CourseSubjectRecord courseSubjectRecord);
}
