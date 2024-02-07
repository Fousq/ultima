package com.example.rbapp.timepackage.service;

import com.example.rbapp.timepackage.controller.api.CourseTimePackageResponse;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Service;

import static com.example.rbapp.jooq.codegen.Tables.COURSE;
import static com.example.rbapp.jooq.codegen.Tables.TIME_PACKAGE;

@Service
public class CourseTimePackageResponseRecordMapper implements RecordMapper<Record, CourseTimePackageResponse> {
    @Override
    public CourseTimePackageResponse map(Record record) {
        Long id = record.getValue(TIME_PACKAGE.ID);
        String type = record.getValue(TIME_PACKAGE.TYPE);
        Integer amount = record.getValue(TIME_PACKAGE.AMOUNT_IN_MINUTES);
        Integer initialAmount = record.getValue(TIME_PACKAGE.INITIAL_AMOUNT_IN_MINUTES);
        Long studentId = record.getValue(TIME_PACKAGE.STUDENT_ID);
        Long courseId = record.getValue(COURSE.ID);
        return new CourseTimePackageResponse(id, type, amount, initialAmount, studentId, courseId);
    }
}
