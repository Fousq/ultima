package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.course.service.CourseService;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.teacher.controller.api.TeacherSaveRequest;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherMapper;
import com.example.rbapp.teacher.service.TeacherRepository;
import com.example.rbapp.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final CourseService courseService;

    @Override
    public void updateTeacherCourseList(List<Teacher> teachers, Long courseId) {
        List<TeacherRecord> teacherCourseRecords = teacherRepository.findAllByCourseId(courseId);

        // TODO refactor to processors (separate classes)
        removeUnlinkedTeachers(teacherCourseRecords, teachers, courseId);
        addNewTeachersToCourse(teacherCourseRecords, teachers, courseId);
    }

    private void removeUnlinkedTeachers(List<TeacherRecord> teacherCourseRecords,
                                        List<Teacher> teachers,
                                        Long courseId) {
        List<Long> teacherIdsToRemove = teacherCourseRecords.stream()
                .map(TeacherRecord::getId)
                .filter(teacherId -> teachers.stream().noneMatch(teacher -> teacher.getId().equals(teacherId)))
                .toList();

        if (!teacherIdsToRemove.isEmpty()) {
            teacherRepository.deleteTeacherCourseByTeacherIdsAndCourseId(teacherIdsToRemove, courseId);
        }
    }

    private void addNewTeachersToCourse(List<TeacherRecord> teacherCourseRecords,
                                        List<Teacher> teachers,
                                        Long courseId) {
        List<Long> teacherIdsToAdd = teachers.stream()
                .map(Teacher::getId)
                .filter(teacherId -> teacherCourseRecords.stream()
                        .noneMatch(teacherCourseRecord -> teacherCourseRecord.getId().equals(teacherId))
                )
                .toList();
        if (!teacherIdsToAdd.isEmpty()) {
            teacherRepository.createTeacherCourse(teacherIdsToAdd, courseId);
        }
    }

    @Override
    public TeacherResponse update(Long id, TeacherSaveRequest teacherSaveRequest) {
        TeacherRecord teacherRecord = teacherMapper.mapSaveRequestToRecord(teacherSaveRequest, id);
        teacherRepository.update(teacherRecord);
        return getById(id);
    }

    @Override
    public List<TeacherResponse> getTeacherList(Boolean includeCourseParticipation) {
        List<TeacherRecord> teacherRecords = teacherRepository.findAll();
        if (includeCourseParticipation) {
            return teacherRecords.stream().map(teacherRecord -> {
                List<CourseRecord> courses = courseService.getTeacherCourses(teacherRecord.getId());
                return teacherMapper.mapRecordToResponse(teacherRecord, courses);
            }).toList();
        }
        return teacherMapper.mapEntityToResponse(teacherRecords);
    }

    public TeacherResponse getById(Long id) {
        return teacherMapper.mapEntityToResponse(getEntityById(id));
    }

    private Teacher getEntityById(Long id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::mapRecordToEntity)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));
    }

    @Override
    public void removeTeachersFromCourse(List<Long> teacherIds, Long courseId) {
        teacherRepository.deleteTeacherCourseByTeacherIdsAndCourseId(teacherIds, courseId);
    }

    @Override
    public void addTeachersToCourse(List<Long> teacherIds, Long courseId) {
        teacherRepository.createTeacherCourse(teacherIds, courseId);
    }

    @Override
    public Teacher create(Teacher teacher) {
        TeacherRecord teacherRecord = teacherMapper.mapEntityToRecord(teacher);
        Long id = teacherRepository.create(teacherRecord);
        return getEntityById(id);
    }

    @Override
    public void updateBitrixId(Teacher teacher, Long bitrixId) {
        teacherRepository.updateBitrixIdById(teacher.getId(), bitrixId);
    }

    @Override
    public TeacherResponse getByUserId(Long userId) {
        return teacherRepository.findByUserId(userId)
                .map(teacherMapper::mapRecordToResponse)
                .orElseThrow(() -> new NotFoundException("Teacher not found by user id"));
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return teacherRepository.existsByUserId(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        teacherRepository.deleteTeacherByUserId(userId);
    }
}
