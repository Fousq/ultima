package com.example.rbapp.studentvideoclass.entity;

import com.example.rbapp.student.entity.Student;
import lombok.Data;

@Data
public class StudentVideoClass {

    private Long videoClassId;

    private Student student;

    private Boolean visited = Boolean.FALSE;

}
