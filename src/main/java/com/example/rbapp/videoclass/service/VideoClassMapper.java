package com.example.rbapp.videoclass.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.VideoClassRecord;
import com.example.rbapp.studentvideoclass.service.StudentVideoClassMapper;
import com.example.rbapp.videoclass.controller.api.VideoClassResponse;
import com.example.rbapp.videoclass.controller.api.VideoClassUpdateRequest;
import com.example.rbapp.videoclass.entity.VideoClass;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class, uses = StudentVideoClassMapper.class)
public interface VideoClassMapper {
    VideoClass mapToEntity(VideoClassRecord videoClass);

    List<VideoClass> mapToEntity(Collection<VideoClassRecord> videoClassList);

    VideoClassRecord mapToRecord(VideoClass videoClass);

    VideoClassRecord mapUpdateRequestToRecord(VideoClassUpdateRequest videoClassUpdateRequest);

    VideoClassResponse mapToResponse(VideoClass videoClass);

    List<VideoClassResponse> mapToResponse(Collection<VideoClass> videoClass);
}
