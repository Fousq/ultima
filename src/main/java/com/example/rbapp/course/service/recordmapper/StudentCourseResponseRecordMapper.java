package com.example.rbapp.course.service.recordmapper;

import com.example.rbapp.course.controller.api.StudentCourseResponse;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Component
public class StudentCourseResponseRecordMapper implements RecordMapper<Record, StudentCourseResponse> {
    @Override
    public StudentCourseResponse map(Record record) {
        Long courseId = record.getValue(COURSE.ID);
        String title = record.getValue(COURSE.TITLE);
        String type = record.getValue(COURSE.TYPE);
        TeacherResponse teacherResponse = mapToTeacherResponse(record);

        return new StudentCourseResponse(courseId, title, type, teacherResponse);
    }

    private TeacherResponse mapToTeacherResponse(Record record) {
        Long teacherId = record.getValue(TEACHER.ID);
        String teacherName = record.getValue(TEACHER.NAME);
        String teacherSurname = record.getValue(TEACHER.SURNAME);
        return new TeacherResponse(teacherId, teacherName, teacherSurname, null, null, null);
    }
}
