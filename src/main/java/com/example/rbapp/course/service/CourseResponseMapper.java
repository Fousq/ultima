package com.example.rbapp.course.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.course.controller.api.CourseParticipationCompactResponse;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface CourseResponseMapper {

    CourseParticipationCompactResponse mapRecordToParticipationCompactResponse(CourseRecord courseRecord);

    List<CourseParticipationCompactResponse> mapRecordToParticipationCompactResponse(Collection<CourseRecord> courseRecords);
}
