package com.example.rbapp.coursesubject.controller.api;

import java.time.ZonedDateTime;

public record RecentCourseSubjectResponse(Long id, String title, ZonedDateTime startAt, String lessonLink) {
}
