package com.example.rbapp.studentcourse.service;

import com.example.rbapp.course.entity.Course;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.studentcourse.entity.StudentCourse;
import com.example.rbapp.student.service.recordmapper.StudentRecordMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Service;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Service
@RequiredArgsConstructor
public class StudentCourseRecordMapper implements RecordMapper<Record, StudentCourse> {

    private final StudentRecordMapper studentRecordMapper;

    @Override
    public StudentCourse map(Record record) {
        StudentCourse studentCourse = new StudentCourse();

        if (record.get(STUDENT.ID) != null) {
            Student student = studentRecordMapper.map(record);
            studentCourse.setStudent(student);
        }

        if (record.get(COURSE.ID) != null) {
            Course course = mapToCourse(record);
            studentCourse.setCourse(course);
        }

        return studentCourse;
    }

    private Course mapToCourse(Record record) {
        Course course = new Course();
        course.setId(record.getValue(COURSE.ID));
        course.setTitle(record.getValue(COURSE.TITLE));
        course.setLessonLink(record.getValue(COURSE.LESSON_LINK));
        course.setDuration(record.get(COURSE.DURATION));
        return course;
    }
}
