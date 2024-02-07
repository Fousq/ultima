package com.example.rbapp.homework.entity;

import lombok.Data;

import java.util.List;

@Data
public class Homework {

    private Long id;

    private String title;

    private String description;

    private List<String> files;

    private Long courseSubjectId;

    private String studentFile;

    private List<Long> studentIds;
}
