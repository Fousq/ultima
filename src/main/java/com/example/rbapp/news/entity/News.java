package com.example.rbapp.news.entity;

import lombok.Data;

@Data
public class News {

    private Long id;

    private String title;

    private String description;

    private String picture; // encoded bytes from front-end

    private Boolean internal;

    private Boolean show;

    private String shortDescription;
}
