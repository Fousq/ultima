package com.example.rbapp.videoclass.entity;

import com.example.rbapp.student.entity.Student;
import com.example.rbapp.studentvideoclass.entity.StudentVideoClass;
import lombok.Data;

import java.util.List;

@Data
public class VideoClass {

    private Long id;

    private String title;

    private String link;

    private Integer duration;

    private Long teacherId;

    private Long courseSubjectId;

    private List<StudentVideoClass> studentList = List.of();
}
