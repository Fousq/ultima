package com.example.rbapp.homework.service;

import com.example.rbapp.homework.controller.api.CourseHomeworkResponse;
import com.example.rbapp.homework.controller.api.HomeworkResponse;
import com.example.rbapp.homework.controller.api.StudentHomeworkResponse;
import com.example.rbapp.homework.entity.Homework;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.homework.entity.StudentHomework;
import com.example.rbapp.jooq.codegen.tables.records.HomeworkRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentHomeworkRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface HomeworkMapper {
    HomeworkResponse mapToResponse(HomeworkRecord homework);

    HomeworkRecord mapToRecord(Homework homework);

    List<HomeworkRecord> mapToRecord(Collection<Homework> homework);

    Homework mapToEntity(HomeworkRecord homeworkRecord);

    List<Homework> mapToEntity(List<HomeworkRecord> homeworks);

    HomeworkResponse mapEntityToResponse(Homework homework);

    List<HomeworkResponse> mapEntityToResponse(Collection<Homework> homework);

    @Mapping(target = "id", source = "homeworkId")
    StudentHomeworkResponse mapToStudentHomeworkResponse(StudentHomeworkRecord studentHomeworkRecord);

    CourseHomeworkResponse mapStudentHomeworkToCourseResponse(StudentHomework studentHomework);

    List<CourseHomeworkResponse> mapStudentHomeworkToCourseResponse(Collection<StudentHomework> studentHomework);
}
