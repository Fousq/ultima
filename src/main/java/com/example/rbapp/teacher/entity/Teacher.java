package com.example.rbapp.teacher.entity;

import lombok.Data;

@Data
public class Teacher {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String bankDetails;

    private Long userId;
}
