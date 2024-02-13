package com.example.rbapp.course.service;

import com.example.rbapp.course.controller.api.CourseParticipationCompactResponse;
import com.example.rbapp.course.controller.api.CourseResponse;
import com.example.rbapp.course.controller.api.CourseSaveRequest;
import com.example.rbapp.course.entity.Course;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.coursesubject.entity.CourseSubject;
import com.example.rbapp.coursesubject.service.CourseSubjectMapper;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import com.example.rbapp.student.service.StudentMapper;
import com.example.rbapp.teacher.service.TeacherMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class, uses = {CourseSubjectMapper.class, TeacherMapper.class, StudentMapper.class})
public interface CourseMapper {
    @Mapping(target = "courseSubjects", source = "courseSubjectList")
    @Mapping(target = "durationList", source = "courseSubjectList", qualifiedByName = "CourseSubjectListToDurationList")
    CourseResponse map(Course course);

    List<CourseResponse> map(Collection<Course> courseList);

    List<Course> mapToEntity(List<CourseRecord> courseRecords);

    @Mapping(target = "courseSubjectList", ignore = true)
    @Mapping(target = "studentList", ignore = true)
    @Mapping(target = "teacherList", ignore = true)
    Course mapToEntity(CourseRecord courseRecord);

    CourseRecord mapToRecord(CourseSaveRequest courseSaveRequest);

    @Mapping(target = "id", source = "id")
    CourseRecord mapToRecord(CourseSaveRequest courseSaveRequest, Long id);

    @Named("CourseSubjectListToDurationList")
    default List<ZonedDateTime> mapCourseSubjectListToDurationList(List<CourseSubject> courseSubjectList) {
        return courseSubjectList.stream().map(CourseSubject::getStartAt).toList();
    }

}
