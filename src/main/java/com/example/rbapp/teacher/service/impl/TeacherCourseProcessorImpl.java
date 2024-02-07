package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherCourseProcessor;
import com.example.rbapp.teacher.service.TeacherRepository;
import com.example.rbapp.teacher.service.operation.TeacherCourseOperationProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherCourseProcessorImpl implements TeacherCourseProcessor {

    private final Collection<TeacherCourseOperationProcessor> teacherCourseOperationProcessors;
    private final TeacherRepository teacherRepository;

    @Override
    public void process(List<Teacher> teachers, Long courseId) {
        List<TeacherRecord> teacherRecords = teacherRepository.findAllByCourseId(courseId);

        teacherCourseOperationProcessors.forEach(
                teacherCourseProcessor -> teacherCourseProcessor.process(teacherRecords, teachers, courseId)
        );
    }
}
