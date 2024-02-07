package com.example.rbapp.teacher.service.operation;

import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.entity.Teacher;

import java.util.List;

public interface TeacherCourseOperationProcessor {
    void process(List<TeacherRecord> teacherRecords, List<Teacher> teachers, Long courseId);
}
