package com.example.rbapp.coursesubject.service.recordmapper;

import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.coursesubject.controller.api.RecentCourseSubjectResponse;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static com.example.rbapp.jooq.codegen.Tables.COURSE;
import static com.example.rbapp.jooq.codegen.Tables.COURSE_SUBJECT;

@Component
public class RecentCourseSubjectResponseRecordMapper implements RecordMapper<Record, RecentCourseSubjectResponse> {
    @Override
    public RecentCourseSubjectResponse map(Record record) {
        Long courseSubjectId = record.getValue(COURSE_SUBJECT.ID);
        String courseSubjectTitle = record.getValue(COURSE_SUBJECT.TITLE);
        ZonedDateTime startAt = record.getValue(COURSE_SUBJECT.START_AT).atZone(DateTimeConstant.ALMATY);
        String lessonLink = record.getValue(COURSE.LESSON_LINK);
        return new RecentCourseSubjectResponse(courseSubjectId, courseSubjectTitle, startAt, lessonLink);
    }
}
