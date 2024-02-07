package com.example.rbapp.student.service.recordmapper;

import com.example.rbapp.student.entity.Student;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Service;

import static com.example.rbapp.jooq.codegen.Tables.STUDENT;

@Service
public class StudentRecordMapper implements RecordMapper<Record, Student> {

    @Override
    public Student map(Record record) {
        Student student = new Student();
        student.setId(record.getValue(STUDENT.ID));
        student.setName(record.getValue(STUDENT.NAME));
        student.setPhone(record.getValue(STUDENT.PHONE));
        student.setEmail(record.getValue(STUDENT.EMAIL));
        student.setSurname(record.getValue(STUDENT.SURNAME));
        student.setCity(record.getValue(STUDENT.CITY));
        student.setMiddleName(record.getValue(STUDENT.MIDDLE_NAME));
        student.setStudyGoal(record.getValue(STUDENT.STUDY_GOAL));
        student.setWishes(record.getValue(STUDENT.WISHES));
        return student;
    }
}
