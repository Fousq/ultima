package com.example.rbapp.homework.entity;

import lombok.Data;

@Data
public class StudentHomework {

    private Long id;

    private String title;

    private String description;

    private Boolean completed = Boolean.FALSE;

    private String file;

    private Long studentId;

    private Boolean inProgress = Boolean.FALSE;

    private String feedback;
}
