package com.example.rbapp.course.entity;

import com.example.rbapp.coursesubject.entity.CourseSubject;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.teacher.entity.Teacher;
import lombok.Data;

import java.util.List;


@Data
public class Course {

    private Long id;

    private String title;

    private String duration;

    private String lessonLink;

    private String type;

    private List<CourseSubject> courseSubjectList;

    private List<Student> studentList;

    private List<Teacher> teacherList;
}
