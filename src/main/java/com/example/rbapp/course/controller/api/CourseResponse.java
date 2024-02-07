package com.example.rbapp.course.controller.api;

import com.example.rbapp.coursesubject.controller.api.CourseSubjectResponse;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.teacher.controller.api.TeacherResponse;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public record CourseResponse(Long id,
                             String title,
                             List<ZonedDateTime> durationList,
                             String lessonLink,
                             String type,
                             Collection<StudentResponse> studentList,
                             Collection<TeacherResponse> teacherList,
                             Collection<CourseSubjectResponse> courseSubjects) {

}
