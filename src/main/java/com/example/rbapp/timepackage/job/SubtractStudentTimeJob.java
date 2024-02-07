package com.example.rbapp.timepackage.job;

import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.coursesubject.service.CourseSubjectService;
import com.example.rbapp.timepackage.service.TimePackageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class SubtractStudentTimeJob {

    private final JobScheduler jobScheduler;
    private final TimePackageService timePackageService;
    private final CourseSubjectService courseSubjectService;

    @Value("${jobs.subtract-student-time.cron}")
    @Autowired
    private String cronExpression;

    @PostConstruct
    public void schedule() {
        jobScheduler.scheduleRecurrently(
                "subtract-student-time",
                cronExpression,
                this::execute
        );
    }

    public void execute() {
        ZonedDateTime today = ZonedDateTime.now(DateTimeConstant.ALMATY);
        ZonedDateTime yesterday = today.minusDays(1);
        courseSubjectService.getCourseSubjectsBetween(yesterday, today)
                .forEach(courseSubject -> timePackageService.subtractTimeForStudentsInCourse(
                                courseSubject.getCourseId(),
                                courseSubject.getDuration())
                );
    }
}
