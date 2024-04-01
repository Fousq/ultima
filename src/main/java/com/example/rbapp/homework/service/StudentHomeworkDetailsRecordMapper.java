package com.example.rbapp.homework.service;

import com.example.rbapp.homework.controller.api.StudentHomeworkDetailsResponse;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Service
public class StudentHomeworkDetailsRecordMapper implements RecordMapper<Record, StudentHomeworkDetailsResponse> {
    @Override
    public StudentHomeworkDetailsResponse map(Record record) {
        Long id = record.getValue(HOMEWORK.ID);
        String title = record.getValue(HOMEWORK.TITLE);
        List<String> files = record.getValue(HOMEWORK.FILES) != null ? List.of(record.getValue(HOMEWORK.FILES))
                : List.of();
        Boolean completed = record.getValue(STUDENT_HOMEWORK.COMPLETED);
        Long studentId = record.getValue(STUDENT.ID);
        String name = record.getValue(STUDENT.NAME);
        String surname = record.getValue(STUDENT.SURNAME);
        Boolean inProgress = record.getValue(STUDENT_HOMEWORK.IN_PROGRESS);
        String feedback = record.getValue(STUDENT_HOMEWORK.FEEDBACK);
        String description = record.getValue(STUDENT_HOMEWORK.DESCRIPTION);

        return new StudentHomeworkDetailsResponse(
                id,
                title,
                description,
                files,
                completed,
                feedback,
                inProgress,
                studentId,
                name,
                surname
        );
    }
}
