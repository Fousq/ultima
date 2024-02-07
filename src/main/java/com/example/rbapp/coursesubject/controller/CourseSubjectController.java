package com.example.rbapp.coursesubject.controller;

import com.example.rbapp.coursesubject.controller.api.CourseSubjectPatchRequest;
import com.example.rbapp.coursesubject.controller.api.CourseSubjectPatchResponse;
import com.example.rbapp.coursesubject.controller.api.CourseSubjectResponse;
import com.example.rbapp.coursesubject.controller.api.RecentCourseSubjectResponse;
import com.example.rbapp.coursesubject.service.CourseSubjectService;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/course/subject")
@RequiredArgsConstructor
public class CourseSubjectController {

    private final CourseSubjectService courseSubjectService;
    private final UserService userService;


    @PatchMapping("/{courseSubjectId}")
    public CourseSubjectPatchResponse patchCourseSubject(@PathVariable Long courseSubjectId,
                                                         CourseSubjectPatchRequest request) {
        return courseSubjectService.update(courseSubjectId, request);
    }

    @PostMapping("/{id}/student")
    public ResponseEntity<Object> addStudentCourseSubject(@PathVariable("id") Long id,
                                                          @RequestHeader(AUTHORIZATION) String token) {
        Long userId = userService.loadUserByToken(token).getId();
        courseSubjectService.addStudentCourseSubject(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/recent")
    public RecentCourseSubjectResponse getRecentStudentCourseSubject(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = userService.loadUserByToken(token).getId();
        return courseSubjectService.getRecentStudentCourseSubject(userId);
    }
}
