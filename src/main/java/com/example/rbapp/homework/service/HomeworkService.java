package com.example.rbapp.homework.service;

import com.example.rbapp.homework.controller.api.HomeworkResponse;
import com.example.rbapp.homework.controller.api.StudentHomeworkDetailsResponse;
import com.example.rbapp.homework.controller.api.StudentHomeworkResponse;
import com.example.rbapp.homework.controller.api.StudentHomeworkUpdateRequest;
import com.example.rbapp.homework.entity.Homework;
import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.homework.entity.StudentHomework;
import com.example.rbapp.jooq.codegen.tables.records.HomeworkRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentHomeworkRecord;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;

    private final HomeworkMapper homeworkMapper;

    private final StudentService studentService;

    public List<Homework> getHomeworkList() {
        return homeworkMapper.mapToEntity(homeworkRepository.findAll());
    }

    public HomeworkResponse getHomework(Long id) {
        return homeworkRepository.findById(id)
                .map(homeworkMapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Homework not found"));
    }

    public void create(Homework homework) {
        HomeworkRecord homeworkRecord = homeworkMapper.mapToRecord(homework);
        Long homeworkId = homeworkRepository.create(homeworkRecord);
        if (homework.getStudentIds() != null) {
            homework.getStudentIds().forEach(studentId -> {
                StudentHomeworkRecord studentHomeworkRecord = new StudentHomeworkRecord();
                studentHomeworkRecord.setStudentId(studentId);
                studentHomeworkRecord.setHomeworkId(homeworkId);
                studentHomeworkRecord.setCompleted(Boolean.FALSE);
                studentHomeworkRecord.setInProgress(Boolean.TRUE);
                studentHomeworkRecord.setFile(homework.getStudentFile());
                homeworkRepository.createStudentHomework(studentHomeworkRecord);
            });
        }
    }

    public void update(List<Homework> homeworkList) {
        List<HomeworkRecord> homeworkRecordList = homeworkMapper.mapToRecord(homeworkList);
        homeworkRepository.batchUpdate(homeworkRecordList);
    }

    public HomeworkResponse update(Homework homework) {
        HomeworkRecord homeworkRecord = homeworkMapper.mapToRecord(homework);
        homeworkRepository.update(homeworkRecord);
        List<Long> requestStudentIds = homework.getStudentIds();
        if (requestStudentIds != null) {
            List<Long> studentIds = homeworkRepository.findAllByHomeworkId(homework.getId()).stream()
                    .map(StudentHomeworkRecord::getStudentId)
                    .toList();
            List<Long> studentIdsToRemove = studentIds.stream()
                    .filter(studentId -> !requestStudentIds.contains(studentId))
                    .toList();
            List<Long> studentIdsToAdd = requestStudentIds.stream()
                    .filter(requestStudentId -> !studentIds.contains(requestStudentId))
                    .toList();
            homeworkRepository.deleteStudentHomeworkByStudentIdAndHomeworkId(studentIdsToRemove, homework.getId());
            homeworkRepository.addStudentHomeworkList(studentIdsToAdd, homework.getId());
        }

        return getHomework(homework.getId());
    }

    public void delete(Long id) {
        homeworkRepository.deleteById(id);
    }

    public List<StudentHomework> getStudentHomeworkListByCourseSubjectId(Long courseSubjectId) {
        return homeworkRepository.findAllByCourseSubjectId(courseSubjectId);
    }

    public List<Homework> getHomeworkListByCourseAndStudent(Long courseId, Long studentId) {
        return homeworkMapper.mapToEntity(homeworkRepository.findAllByCourseIdAndStudentId(courseId, studentId));
    }

    public Optional<Homework> findHomeworkByCourseSubjectId(Long courseSubjectId) {
        return homeworkRepository.findByCourseSubjectId(courseSubjectId)
                .map(homeworkMapper::mapToEntity);
    }

    public void updateStudentHomework(Collection<StudentHomework> studentHomeworks, Long id) {
        List<StudentHomeworkRecord> studentHomeworkRecords = studentHomeworks.stream().map(studentHomework -> {
            StudentHomeworkRecord studentHomeworkRecord = new StudentHomeworkRecord();
            studentHomeworkRecord.setStudentId(studentHomework.getStudentId());
            studentHomeworkRecord.setHomeworkId(id);
            studentHomeworkRecord.setCompleted(studentHomework.getCompleted());
            studentHomeworkRecord.setInProgress(studentHomework.getInProgress());
            studentHomeworkRecord.setFeedback(studentHomework.getFeedback());
            studentHomeworkRecord.setFile(studentHomework.getFile());
            return studentHomeworkRecord;
        }).toList();
        homeworkRepository.updateStudentHomework(studentHomeworkRecords);
    }

    public StudentHomeworkResponse updateStudentHomework(Long homeworkId,
                                                         Long studentId,
                                                         StudentHomeworkUpdateRequest request) {
        StudentHomeworkRecord studentHomeworkRecord = new StudentHomeworkRecord();
        studentHomeworkRecord.setStudentId(studentId);
        studentHomeworkRecord.setHomeworkId(homeworkId);
        studentHomeworkRecord.setCompleted(request.completed());
        studentHomeworkRecord.setInProgress(request.progress());
        studentHomeworkRecord.setFeedback(request.feedback());
        studentHomeworkRecord.setFile(request.file());
        homeworkRepository.findStudentHomeworkByHomeworkIdAndStudentId(homeworkId, studentId)
                .ifPresentOrElse(
                        ignored -> homeworkRepository.updateStudentHomework(studentHomeworkRecord),
                        () -> homeworkRepository.createStudentHomework(studentHomeworkRecord)
                );
        return homeworkRepository.findStudentHomeworkByHomeworkIdAndStudentId(homeworkId, studentId)
                .map(homeworkMapper::mapToStudentHomeworkResponse)
                .orElseThrow(() -> new NotFoundException("Student homework cannot be found"));
    }

    public List<StudentHomeworkDetailsResponse> getStudentHomeworkListByCourse(Long courseId) {
        return homeworkRepository.findAllByCourseId(courseId);
    }

    public void createStudentHomework(List<StudentHomework> studentHomeworks, Long id) {
        List<StudentHomeworkRecord> studentHomeworkRecords = studentHomeworks.stream().map(studentHomework -> {
            StudentHomeworkRecord studentHomeworkRecord = new StudentHomeworkRecord();
            studentHomeworkRecord.setStudentId(studentHomework.getStudentId());
            studentHomeworkRecord.setHomeworkId(id);
            studentHomeworkRecord.setCompleted(studentHomework.getCompleted());
            studentHomeworkRecord.setInProgress(studentHomework.getInProgress());
            studentHomeworkRecord.setFeedback(studentHomework.getFeedback());
            studentHomeworkRecord.setFile(studentHomework.getFile());
            return studentHomeworkRecord;
        }).toList();
        homeworkRepository.batchCreateStudentHomework(studentHomeworkRecords);
    }

    public StudentHomeworkResponse updateStudentCourseSubjectHomework(Long courseSubjectId, Long userId,
                                                                      StudentHomeworkUpdateRequest request) {
        Long homeworkId = homeworkRepository.findByCourseSubjectId(courseSubjectId)
                .map(HomeworkRecord::getId)
                .orElseThrow(() -> new NotFoundException("Homework not found by courseSubjectId"));
        StudentResponse student = studentService.getStudentResponseByUserId(userId);
        return updateStudentHomework(homeworkId, student.id(), request);
    }
}
