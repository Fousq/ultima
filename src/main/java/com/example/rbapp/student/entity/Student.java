package com.example.rbapp.student.entity;

import lombok.Data;

import java.time.LocalDate;
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

    private LocalDate birthday;

    private String parentPhone;

    private String langLevel;

    private Long userId;

    private List<Long> courseIds;

    private Long bitrixContactId;
}
