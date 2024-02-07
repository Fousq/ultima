package com.example.rbapp.timepackage.service;

import com.example.rbapp.jooq.codegen.tables.records.TimePackageRecord;
import com.example.rbapp.timepackage.controller.api.CourseTimePackageResponse;

import java.util.List;
import java.util.Optional;

public interface TimePackageRepository {
    Long create(TimePackageRecord timePackageRecord);

    Optional<TimePackageRecord> findById(Long id);

    Optional<TimePackageRecord> findByStudentPhoneAndType(String studentPhone, String type);

    void updateAllTimeAmount(Long id, int amount, int initialAmount);

    List<TimePackageRecord> findAllByCourseId(Long courseId);

    void batchUpdateAmount(List<TimePackageRecord> timePackageRecords);

    boolean existsByTypeAndStudentId(String type, Long studentId);

    Optional<TimePackageRecord> findByUserIdAndCourseId(Long userId, Long courseId);

    List<TimePackageRecord> findAllByStudentId(Long studentId);

    List<CourseTimePackageResponse> findAllCourseTimePackageByStudentId(Long studentId);
}
