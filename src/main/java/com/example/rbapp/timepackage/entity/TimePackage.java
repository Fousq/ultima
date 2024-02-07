package com.example.rbapp.timepackage.entity;

import lombok.Data;

@Data
public class TimePackage {

    private Long id;
    private Integer amount;
    private Integer initialAmount;
    private String type;
    private Long studentId;
}
