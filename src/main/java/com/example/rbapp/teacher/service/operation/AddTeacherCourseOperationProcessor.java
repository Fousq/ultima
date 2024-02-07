package com.example.rbapp.teacher.service.operation;

import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AddTeacherCourseOperationProcessor implements TeacherCourseOperationProcessor {

    private final TeacherService teacherService;

    @Override
    public void process(List<TeacherRecord> teacherRecords, List<Teacher> teachers, Long courseId) {
        List<Long> teacherIdsToAdd = teachers.stream()
                .map(Teacher::getId)
                .filter(isNotExistsInDB(teacherRecords))
                .toList();
        teacherService.addTeachersToCourse(teacherIdsToAdd, courseId);
    }

    private Predicate<Long> isNotExistsInDB(List<TeacherRecord> teacherRecords) {
        return teacherId -> teacherRecords.stream()
                .noneMatch(
                        teacherCourseRecord -> teacherCourseRecord.getId().equals(teacherId)
                );
    }
}
