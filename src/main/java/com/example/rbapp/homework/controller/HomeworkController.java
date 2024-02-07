package com.example.rbapp.homework.controller;

import com.example.rbapp.homework.controller.api.*;
import com.example.rbapp.homework.entity.Homework;
import com.example.rbapp.homework.service.HomeworkMapper;
import com.example.rbapp.homework.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;
    private final HomeworkMapper homeworkMapper;

    @GetMapping
    public List<HomeworkResponse> homeworkList() {
        return homeworkMapper.mapEntityToResponse(homeworkService.getHomeworkList());
    }

    @GetMapping("/{id}")
    public HomeworkResponse getHomework(@PathVariable("id") Long id) {
        return homeworkService.getHomework(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Homework homework) {
        homeworkService.create(homework);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public HomeworkResponse update(@RequestBody Homework homework) {
        return homeworkService.update(homework);
    }

    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        homeworkService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/course/{courseId}/student/{studentId}")
    public List<HomeworkResponse> getStudentHomework(@PathVariable("courseId") Long courseId,
                                                     @PathVariable("studentId") Long studentId) {
        return homeworkMapper.mapEntityToResponse(homeworkService.getHomeworkListByCourseAndStudent(courseId, studentId));
    }

    @PutMapping("/{homeworkId}/student/{studentId}")
    public StudentHomeworkResponse updateStudentHomework(@PathVariable("homeworkId") Long homeworkId,
                                                         @PathVariable("studentId") Long studentId,
                                                         @RequestBody StudentHomeworkUpdateRequest request) {
        return homeworkService.updateStudentHomework(homeworkId, studentId, request);
    }

    @GetMapping("/{courseId}/student")
    public List<StudentHomeworkDetailsResponse> getStudentHomeworkDetails(@PathVariable("courseId") Long courseId) {
        return homeworkService.getStudentHomeworkListByCourse(courseId);
    }

    @PutMapping("course/subject/{courseSubjectId}/student/{userId}")
    public StudentHomeworkResponse updateStudentCourseSubjectHomework(@PathVariable("courseSubjectId") Long courseSubjectId,
                                                                      @PathVariable("userId") Long userId,
                                                                      @RequestBody StudentHomeworkUpdateRequest request) {
        return homeworkService.updateStudentCourseSubjectHomework(courseSubjectId, userId, request);
    }
}
