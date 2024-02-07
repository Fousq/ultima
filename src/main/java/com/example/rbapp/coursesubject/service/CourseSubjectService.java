package com.example.rbapp.coursesubject.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.coursesubject.controller.api.*;
import com.example.rbapp.coursesubject.entity.CourseSubject;
import com.example.rbapp.homework.entity.Homework;
import com.example.rbapp.homework.entity.StudentHomework;
import com.example.rbapp.homework.service.HomeworkService;
import com.example.rbapp.jooq.codegen.tables.records.CourseSubjectRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentRecord;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.student.service.StudentMapper;
import com.example.rbapp.student.service.StudentRepository;
import com.example.rbapp.timepackage.service.TimePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CourseSubjectService {

    private final CourseSubjectRepository courseSubjectRepository;
    private final HomeworkService homeworkService;
    private final StudentRepository studentRepository;
    private final CourseSubjectMapper courseSubjectMapper;
    private final StudentMapper studentMapper;
    private final TimePackageService timePackageService;

    public List<CourseSubject> getCourseSubjectsByCourseId(Long courseId) {
        var courseSubjectRecords = courseSubjectRepository.findAllByCourseId(courseId);
        List<CourseSubject> courseSubjectList = courseSubjectMapper.mapToEntity(courseSubjectRecords);
        courseSubjectList.forEach(courseSubject -> {
            var courseSubjectId = courseSubject.getId();
            var homework = homeworkService.findHomeworkByCourseSubjectId(courseSubjectId).orElse(null);
            courseSubject.setHomework(homework);
            var homeworkList = homeworkService.getStudentHomeworkListByCourseSubjectId(courseSubjectId);
            courseSubject.setStudentHomeworkList(homeworkList);
            List<StudentRecord> studentRecordList = studentRepository.findAllByCourseSubjectId(courseSubjectId);
            List<Student> studentList = studentMapper.mapToEntity(studentRecordList);
            courseSubject.setStudentList(studentList);
        });
        return courseSubjectList;
    }

    public void create(List<CourseSubject> courseSubjects, Long courseId) {
        List<CourseSubjectRecord> courseSubjectRecords = courseSubjectMapper.mapToRecord(courseSubjects, courseId);
        courseSubjectRepository.batchCreate(courseSubjectRecords);
    }

    public void deleteFromCourse(Long courseId) {
        courseSubjectRepository.deleteAllByCourseId(courseId);
    }

    // TODO refactor
    @Transactional
    public void update(List<CourseSubjectUpdateRequest> courseSubjectsUpdateRequests, Long courseId) {
        List<CourseSubject> courseSubjectListToUpdate = courseSubjectsUpdateRequests.stream()
                .filter(courseSubject -> nonNull(courseSubject.id()))
                .map(courseSubjectMapper::mapUpdateRequestToEntity)
                .toList();
        List<CourseSubjectRecord> courseSubjectRecords = courseSubjectMapper.mapToRecord(courseSubjectListToUpdate, courseId);
        courseSubjectRepository.batchUpdate(courseSubjectRecords);
        courseSubjectListToUpdate.forEach(courseSubject ->
                        homeworkService.findHomeworkByCourseSubjectId(courseSubject.getId())
                            .ifPresentOrElse(homework -> {
                                var requestHomework = courseSubject.getHomework();
                                homework.setTitle(requestHomework.getTitle());
                                homework.setDescription(requestHomework.getDescription());
                                homework.setFiles(requestHomework.getFiles());
                                homeworkService.update(homework);
                            }, () -> {
                                Homework homework = courseSubject.getHomework();
                                if (isNull(homework) || isNull(homework.getTitle())) {
                                    return;
                                }
                                homework.setCourseSubjectId(courseSubject.getId());
                                homeworkService.create(homework);
                            })
                );
        courseSubjectListToUpdate.forEach(courseSubject -> {
            List<StudentHomework> studentHomeworkList = courseSubject.getStudentHomeworkList();
            Optional<Homework> homeworkOptional = homeworkService.findHomeworkByCourseSubjectId(courseSubject.getId());
            if (homeworkOptional.isPresent()) {
                var homework = homeworkOptional.get();
                List<StudentHomework> dbStudentHomeworks = homeworkService.getStudentHomeworkListByCourseSubjectId(courseSubject.getId());

                List<StudentHomework> studentHomeworksToUpdate = studentHomeworkList.stream()
                        .filter(studentHomework -> studentHomeworkList.stream()
                                .anyMatch(dbStudentHomework -> Objects.equals(studentHomework.getStudentId(), dbStudentHomework.getStudentId()))
                        ).toList();
                homeworkService.updateStudentHomework(studentHomeworksToUpdate, homework.getId());

                List<StudentHomework> studentHomeworksToCreate = studentHomeworkList.stream()
                        .filter(studentHomework -> dbStudentHomeworks.stream()
                                .noneMatch(
                                        dbStudentHomework -> Objects.equals(dbStudentHomework.getStudentId(), studentHomework.getStudentId())
                                )
                        )
                        .toList();
                homeworkService.createStudentHomework(studentHomeworksToCreate, homework.getId());
            }
        });

        courseSubjectListToUpdate.forEach(courseSubject -> {
            Long courseSubjectId = courseSubject.getId();
            List<Long> studentIdList = studentRepository.findAllIdsByCourseSubjectId(courseSubjectId);
            List<Long> studentIdsToAdd = courseSubject.getStudentList().stream().map(Student::getId)
                    .filter(studentId -> !studentIdList.contains(studentId))
                    .toList();
            courseSubjectRepository.createStudentCourseSubject(studentIdsToAdd, courseSubjectId);

            List<Long> studentIdsToRemove = studentIdList.stream()
                    .filter(studentId -> courseSubject.getStudentList().stream()
                            .noneMatch(student -> Objects.equals(student.getId(), studentId))
                    ).toList();
            courseSubjectRepository.deleteStudentCourseSubject(studentIdsToRemove, courseSubjectId);
        });

        List<CourseSubject> courseSubjectList = getCourseSubjectsByCourseId(courseId);
        List<Long> courseSubjectIdsToRemove = courseSubjectList.stream()
                .map(CourseSubject::getId)
                .filter(courseSubjectId ->
                        courseSubjectsUpdateRequests.stream()
                                .map(CourseSubjectUpdateRequest::id)
                                .noneMatch(
                                        courseSubjectsUpdateRequestId ->
                                                Objects.equals(courseSubjectId, courseSubjectsUpdateRequestId)
                                ))
                .toList();
        courseSubjectRepository.deleteAll(courseSubjectIdsToRemove);

        List<CourseSubject> courseSubjectListToCreate = courseSubjectsUpdateRequests.stream()
                .filter(courseSubject -> isNull(courseSubject.id()))
                .map(courseSubjectMapper::mapUpdateRequestToEntity)
                .toList();
        create(courseSubjectListToCreate, courseId);
    }

    public CourseSubjectPatchResponse update(Long id, CourseSubjectPatchRequest request) {
        updateCompleted(request.completed(), id);
        CourseSubject courseSubject = getCourseSubjectById(id);
        return courseSubjectMapper.mapEntityToPatchResponse(courseSubject);
    }

    private CourseSubject getCourseSubjectById(Long id) {
        return courseSubjectRepository.findById(id)
                .map(courseSubjectMapper::mapRecordToEntity)
                .orElseThrow(() -> new NotFoundException("Course subject not found"));
    }

    private void updateCompleted(Boolean actualCompletedStatus, Long courseSubjectId) {
        CourseSubject courseSubject = getCourseSubjectById(courseSubjectId);
        courseSubjectRepository.updateCompleted(courseSubjectId, actualCompletedStatus);
        if (Boolean.FALSE.equals(courseSubject.getCompleted()) && Boolean.TRUE.equals(actualCompletedStatus)) {
            timePackageService.subtractTimeForStudentsInCourse(courseSubjectId, 0);
        }
    }

    public List<CourseSubject> getCourseSubjectsBetween(ZonedDateTime yesterday, ZonedDateTime today) {
        List<CourseSubjectRecord> courseSubjectRecords = courseSubjectRepository.findAllByStartAtBetween(yesterday, today);
        return courseSubjectMapper.mapToEntity(courseSubjectRecords);
    }

    public void addStudentCourseSubject(Long id, Long userId) {
        courseSubjectRepository.createStudentCourseSubjectByUserId(id, userId);
    }

    public RecentCourseSubjectResponse getRecentStudentCourseSubject(Long userId) {
        return courseSubjectRepository.findRecentByUserId(userId)
                .orElse(new RecentCourseSubjectResponse(null, null, null, null));
    }
}
