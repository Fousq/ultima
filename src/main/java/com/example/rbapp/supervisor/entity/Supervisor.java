package com.example.rbapp.supervisor.entity;

import lombok.Data;

@Data
public class Supervisor {

    private String id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Long userId;
    private Long bitrixId;
}
