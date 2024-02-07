package com.example.rbapp.studentcourse.entity;

import com.example.rbapp.course.entity.Course;
import com.example.rbapp.student.entity.Student;
import lombok.Data;

@Data
public class StudentCourse {

    private Course course;

    private Student student;
}
