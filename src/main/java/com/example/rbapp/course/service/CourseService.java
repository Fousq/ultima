package com.example.rbapp.course.service;

import com.example.rbapp.course.controller.api.CourseSaveRequest;
import com.example.rbapp.course.controller.api.StudentCourseResponse;
import com.example.rbapp.course.entity.Course;
import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<Course> getCourseList() {
        List<CourseRecord> courseRecords = courseRepository.findAll();
        return courseMapper.mapToEntity(courseRecords);
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::mapToEntity)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @Transactional
    public Long create(CourseSaveRequest course) {
        CourseRecord courseRecord = courseMapper.mapToRecord(course);
        return courseRepository.create(courseRecord);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public void update(CourseSaveRequest courseSaveRequest, Long id) {
        CourseRecord courseRecord = courseMapper.mapToRecord(courseSaveRequest, id);
        courseRepository.update(courseRecord);
    }

    public Long getIdByTitle(String title) {
        return courseRepository.findIdByTitle(title)
                .orElseThrow(() -> new NotFoundException("Course not found by its title"));
    }

    public List<StudentCourseResponse> getStudentCourseByUserId(Long userId) {
        return courseRepository.findAllByUserId(userId);
    }
}
