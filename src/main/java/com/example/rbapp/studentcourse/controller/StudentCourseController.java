package com.example.rbapp.studentcourse.controller;

import com.example.rbapp.course.controller.api.CourseStudentResponse;
import com.example.rbapp.studentcourse.controller.api.StudentCourseCreateRequest;
import com.example.rbapp.studentcourse.service.StudentCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/course")
@RequiredArgsConstructor
public class StudentCourseController {

    private final StudentCourseService studentCourseService;

    @PostMapping
    public CourseStudentResponse createStudentCourse(@RequestBody StudentCourseCreateRequest request) {
        return studentCourseService.createStudentCourse(request);
    }
}
