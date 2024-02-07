package com.example.rbapp.videoclass.controller;

import com.example.rbapp.videoclass.controller.api.VideoClassResponse;
import com.example.rbapp.videoclass.controller.api.VideoClassUpdateRequest;
import com.example.rbapp.videoclass.entity.VideoClass;
import com.example.rbapp.videoclass.service.VideoClassMapper;
import com.example.rbapp.videoclass.service.VideoClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/video/class")
@RequiredArgsConstructor
public class VideoClassController {

    private final VideoClassMapper videoClassMapper;
    private final VideoClassService videoClassService;

    @GetMapping
    public List<VideoClassResponse> videoClasses() {
        return videoClassMapper.mapToResponse(videoClassService.getVideoClassList());
    }

    @GetMapping("/{id}")
    public VideoClassResponse getVideoClass(@PathVariable("id") Long id) {
        return videoClassMapper.mapToResponse(videoClassService.getVideoClass(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody VideoClass videoClass) {
        videoClassService.create(videoClass);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public VideoClassResponse update(@RequestBody VideoClassUpdateRequest videoClass) {
        return videoClassMapper.mapToResponse(videoClassService.update(videoClass));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        videoClassService.delete(id);
        return ResponseEntity.ok().build();
    }
}
