package com.example.rbapp.homework.service;

import com.example.rbapp.homework.entity.StudentHomework;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Service;


import static com.example.rbapp.jooq.codegen.Tables.HOMEWORK;
import static com.example.rbapp.jooq.codegen.Tables.STUDENT_HOMEWORK;

@Service
public class StudentHomeworkRecordMapper implements RecordMapper<Record, StudentHomework> {

    @Override
    public StudentHomework map(Record record) {
        StudentHomework studentHomework = new StudentHomework();
        studentHomework.setId(record.getValue(HOMEWORK.ID));
        studentHomework.setTitle(record.getValue(HOMEWORK.TITLE));
        studentHomework.setDescription(record.getValue(HOMEWORK.DESCRIPTION));
        studentHomework.setCompleted(record.getValue(STUDENT_HOMEWORK.COMPLETED));
        studentHomework.setInProgress(record.getValue(STUDENT_HOMEWORK.IN_PROGRESS));
        studentHomework.setFeedback(record.getValue(STUDENT_HOMEWORK.FEEDBACK));
        studentHomework.setStudentId(record.getValue(STUDENT_HOMEWORK.STUDENT_ID));
        studentHomework.setFile(record.getValue(STUDENT_HOMEWORK.FILE));
        return studentHomework;
    }
}
