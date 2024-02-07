package com.example.rbapp.coursesubject.controller.api;

import com.example.rbapp.homework.controller.api.CourseHomeworkResponse;
import com.example.rbapp.student.controller.api.StudentResponse;

import java.time.ZonedDateTime;
import java.util.List;

public record CourseSubjectResponse(Long id,
                                    String title,
                                    String description,
                                    List<String> files,
                                    Boolean completed,
                                    Integer duration,
                                    ZonedDateTime startAt,
                                    String homeworkTitle,
                                    String homeworkDescription,
                                    List<String> homeworkFiles,
                                    List<CourseHomeworkResponse> homeworkList,
                                    List<StudentResponse> studentList) {
}
