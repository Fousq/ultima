package com.example.rbapp.course.controller;

import com.example.rbapp.course.controller.api.CourseResponse;
import com.example.rbapp.course.controller.api.CourseSaveRequest;
import com.example.rbapp.course.controller.api.StudentCourseResponse;
import com.example.rbapp.course.entity.Course;
import com.example.rbapp.course.service.CourseDetailsService;
import com.example.rbapp.course.service.CourseMapper;
import com.example.rbapp.course.service.CourseService;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseDetailsService courseDetailsService;
    private final CourseMapper courseMapper;
    private final UserService userService;

    @GetMapping
    public List<CourseResponse> courseList() {
        List<Course> courseList = courseDetailsService.getCourseList();
        return courseMapper.map(courseList);
    }

    @GetMapping("/{id}")
    public CourseResponse getCourse(@PathVariable("id") Long id) {
        Course course = courseDetailsService.getCourse(id);
        return courseMapper.map(course);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CourseSaveRequest course) {
        courseDetailsService.create(course);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public List<CourseResponse> update(@RequestBody List<CourseSaveRequest> course) {
        return courseMapper.map(courseDetailsService.update(course));
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable("id") Long id, @RequestBody CourseSaveRequest course) {
        return courseMapper.map(courseDetailsService.update(course, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student")
    public List<StudentCourseResponse> studentCourseList(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = userService.loadUserByToken(token).getId();
        return courseService.getStudentCourseByUserId(userId);
    }

}
