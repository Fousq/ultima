package com.example.rbapp.coursesubject.entity;

import com.example.rbapp.homework.entity.Homework;
import com.example.rbapp.homework.entity.StudentHomework;
import com.example.rbapp.student.entity.Student;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class CourseSubject {

    private Long id;

    private String title;

    private String description;

    private List<String> files = List.of();

    private Integer duration = 0;

    private ZonedDateTime startAt;

    private Boolean completed = Boolean.FALSE;

    private Long courseId;

    private Homework homework;

    private List<StudentHomework> studentHomeworkList;

    private List<Student> studentList;
}
