package com.example.rbapp.teacher.service;

import com.example.rbapp.teacher.entity.Teacher;

import java.util.List;

public interface TeacherCourseProcessor {
    void process(List<Teacher> teachers, Long courseId);
}
