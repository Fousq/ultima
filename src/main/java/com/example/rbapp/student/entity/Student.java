package com.example.rbapp.student.entity;

import lombok.Data;

import java.util.List;

@Data
public class Student {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String middleName;

    private String city;

    private String studyGoal;

    private String wishes;

    private Long userId;

    private List<Long> courseIds;
}
