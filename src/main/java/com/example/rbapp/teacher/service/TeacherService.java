package com.example.rbapp.teacher.service;

import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.teacher.controller.api.TeacherSaveRequest;
import com.example.rbapp.teacher.entity.Teacher;

import java.util.List;

public interface TeacherService {
    void updateTeacherCourseList(List<Teacher> teachers, Long courseId);

    TeacherResponse update(Long id, TeacherSaveRequest teacherSaveRequest);

    List<TeacherResponse> getTeacherList(Boolean includeCourseParticipation);

    TeacherResponse getById(Long id);

    void removeTeachersFromCourse(List<Long> teacherIds, Long courseId);

    void addTeachersToCourse(List<Long> teacherIds, Long courseId);

    Teacher create(Teacher teacher);

    void updateBitrixId(Teacher teacher, Long bitrixId);

    TeacherResponse getByUserId(Long userId);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
