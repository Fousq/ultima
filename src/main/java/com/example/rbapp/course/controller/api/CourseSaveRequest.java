package com.example.rbapp.course.controller.api;

import com.example.rbapp.coursesubject.controller.api.CourseSubjectUpdateRequest;
import com.example.rbapp.student.controller.api.StudentCourseRequest;
import com.example.rbapp.teacher.entity.Teacher;

import java.util.List;

public record CourseSaveRequest(Long id,
                                String title,
                                String duration,
                                String lessonLink,
                                String type,
                                List<StudentCourseRequest> studentList,
                                List<Teacher> teacherList,
                                List<CourseSubjectUpdateRequest> courseSubjects) {
}
