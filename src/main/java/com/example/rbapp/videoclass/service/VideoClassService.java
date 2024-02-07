package com.example.rbapp.videoclass.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.jooq.codegen.tables.records.VideoClassRecord;
import com.example.rbapp.studentvideoclass.entity.StudentVideoClass;
import com.example.rbapp.studentvideoclass.service.StudentVideoClassService;
import com.example.rbapp.videoclass.controller.api.VideoClassUpdateRequest;
import com.example.rbapp.videoclass.entity.VideoClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoClassService {

    private final VideoClassRepository videoClassRepository;
    private final StudentVideoClassService studentVideoClassService;
    private final VideoClassMapper videoClassMapper;

    public List<VideoClass> getVideoClassList() {
        List<VideoClass> videoClassList = videoClassMapper.mapToEntity(videoClassRepository.findAll());
        videoClassList.forEach(this::fetchDetails);
        return videoClassList;
    }

    public VideoClass getVideoClass(Long id) {
        return videoClassRepository.findById(id)
                .map(videoClassMapper::mapToEntity)
                .map(this::fetchDetails)
                .orElseThrow(() -> new NotFoundException("Video class not found"));
    }

    public void create(VideoClass videoClass) {
        VideoClassRecord videoClassRecord = videoClassMapper.mapToRecord(videoClass);
        videoClassRepository.create(videoClassRecord);
    }

    public VideoClass update(VideoClassUpdateRequest videoClass) {
        VideoClassRecord videoClassRecord = videoClassMapper.mapUpdateRequestToRecord(videoClass);
        videoClassRepository.update(videoClassRecord);
        studentVideoClassService.updateList(videoClass.studentList(), videoClass.id());
        return getVideoClass(videoClass.id());
    }

    public void delete(Long id) {
        videoClassRepository.deleteById(id);
    }

    private VideoClass fetchDetails(VideoClass videoClass) {
        List<StudentVideoClass> studentList = studentVideoClassService.getByVideoClassId(videoClass.getId());
        videoClass.setStudentList(studentList);
        return videoClass;
    }
}
