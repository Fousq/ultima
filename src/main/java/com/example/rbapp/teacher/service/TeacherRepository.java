package com.example.rbapp.teacher.service;

import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {
    Long create(TeacherRecord teacher);

    Optional<TeacherRecord> findById(Long id);

    List<TeacherRecord> findAll();

    List<TeacherRecord> findAllByCourseId(Long courseId);

    void createTeacherCourse(List<Long> teacherIds, Long courseId);

    void deleteAllTeacherCourseByCourseId(Long courseId);

    void deleteTeacherCourseByTeacherIdsAndCourseId(List<Long> teacherIds, Long courseId);

    void update(TeacherRecord teacherRecord);

    void updateBitrixIdById(Long id, Long bitrixId);

    Optional<TeacherRecord> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    void deleteTeacherByUserId(Long userId);
}
