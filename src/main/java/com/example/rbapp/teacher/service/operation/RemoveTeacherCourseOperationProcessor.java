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
public class RemoveTeacherCourseOperationProcessor implements TeacherCourseOperationProcessor {

    private final TeacherService teacherService;

    @Override
    public void process(List<TeacherRecord> teacherRecords, List<Teacher> teachers, Long courseId) {
        List<Long> teacherIdsToRemove = teacherRecords.stream()
                .map(TeacherRecord::getId)
                .filter(isRemovedFromRequest(teachers))
                .toList();

        teacherService.removeTeachersFromCourse(teacherIdsToRemove, courseId);
    }

    private Predicate<Long> isRemovedFromRequest(List<Teacher> teachers) {
        return teacherId -> teachers.stream().noneMatch(teacher -> teacher.getId().equals(teacherId));
    }
}
