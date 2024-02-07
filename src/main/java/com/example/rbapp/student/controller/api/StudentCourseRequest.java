package com.example.rbapp.student.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StudentCourseRequest(Long id, Long timePackage) {

}
