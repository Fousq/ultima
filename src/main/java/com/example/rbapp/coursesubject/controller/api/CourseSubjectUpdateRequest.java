package com.example.rbapp.coursesubject.controller.api;

import com.example.rbapp.homework.controller.api.CourseHomeworkResponse;
import com.example.rbapp.student.controller.api.StudentResponse;

import java.time.Instant;
import java.util.List;

public record CourseSubjectUpdateRequest(Long id,
                                         String title,
                                         String description,
                                         List<String> files,
                                         Boolean completed,
                                         Integer duration,
                                         Instant startAt,
                                         String homeworkTitle,
                                         String homeworkDescription,
                                         List<String> homeworkFiles,
                                         List<CourseHomeworkResponse> homeworkList,
                                         List<StudentResponse> studentList) {
}
