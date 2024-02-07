package com.example.rbapp.timepackage.controller;

import com.example.rbapp.timepackage.controller.api.CourseTimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageSaveRequest;
import com.example.rbapp.timepackage.service.TimePackageService;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/time/package")
@RequiredArgsConstructor
public class TimePackageController {

    private final TimePackageService timePackageService;
    private final UserService userService;

    @PostMapping
    public TimePackageResponse createTimePackage(@RequestBody TimePackageSaveRequest timePackageSaveRequest) {
        return timePackageService.create(timePackageSaveRequest);
    }

    @PatchMapping
    public TimePackageResponse updateTimePackage(@RequestBody TimePackageSaveRequest timePackageSaveRequest) {
        return timePackageService.update(timePackageSaveRequest);
    }

    @GetMapping("/student/course/{courseId}")
    public TimePackageResponse getTimePackage(@RequestHeader(AUTHORIZATION) String token,
                                              @PathVariable("courseId") Long courseId) {
        Long userId = userService.loadUserByToken(token).getId();
        return timePackageService.getUserTimePackageForCourse(userId, courseId);
    }

    @GetMapping("/student/{studentId}")
    public List<CourseTimePackageResponse> getStudentTimePackage(@PathVariable("studentId") Long studentId) {
        return timePackageService.getStudentTimePackageList(studentId);
    }
}
