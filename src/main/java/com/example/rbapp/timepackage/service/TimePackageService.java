package com.example.rbapp.timepackage.service;

import com.example.rbapp.timepackage.controller.api.CourseTimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageSaveRequest;
import com.example.rbapp.timepackage.entity.TimePackage;

import java.util.List;

public interface TimePackageService {
    TimePackageResponse create(TimePackageSaveRequest request);

    TimePackageResponse update(TimePackageSaveRequest request);

    void subtractTimeForStudentsInCourse(Long courseId, int amount);

    TimePackage createStudentPackage(String email, String type, Integer amount);

    TimePackageResponse getUserTimePackageForCourse(Long userId, Long courseId);

    List<CourseTimePackageResponse> getStudentTimePackageList(Long studentId);
}
