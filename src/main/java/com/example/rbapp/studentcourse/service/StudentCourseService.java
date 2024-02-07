package com.example.rbapp.studentcourse.service;

import com.example.rbapp.course.controller.api.CourseStudentResponse;
import com.example.rbapp.course.service.CourseService;
import com.example.rbapp.jooq.codegen.tables.records.StudentCourseRecord;
import com.example.rbapp.student.controller.api.StudentCourseRequest;
import com.example.rbapp.student.service.StudentService;
import com.example.rbapp.studentcourse.controller.api.StudentCourseCreateRequest;
import com.example.rbapp.studentcourse.entity.StudentCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final StudentCourseMapper studentCourseMapper;
    private final CourseService courseService;
    private final StudentService studentService;

    public List<StudentCourse> getStudentsByCourseId(Long courseId) {
        return studentCourseRepository.findAllByCourseId(courseId);
    }

    public void addStudentsToCourse(List<StudentCourseRequest> studentCourseRequests, Long courseId) {
        List<StudentCourseRecord> studentCourseRecordList = studentCourseMapper.mapToRecord(studentCourseRequests, courseId);
        studentCourseRepository.batchCreate(studentCourseRecordList);
    }

    public void deleteStudentsFromCourse(Long courseId) {
        studentCourseRepository.deleteStudentCourseListByCourseId(courseId);
    }

    public void updateStudentCourseList(List<StudentCourseRequest> studentCourseRequests, Long courseId) {
        List<StudentCourse> studentCourseList = studentCourseRepository.findAllByCourseId(courseId);

        // TODO refactor to processors (separate classes)
        removeNoneExistingStudentCourse(studentCourseList, studentCourseRequests, courseId);
        addNewStudentCourse(studentCourseList, studentCourseRequests, courseId);
    }

    private void removeNoneExistingStudentCourse(List<StudentCourse> studentCourseList,
                                                 List<StudentCourseRequest> studentCourseRequests,
                                                 Long courseId) {
        List<Long> studentIdsToRemove = studentCourseList.stream()
                .map(studentCourse -> studentCourse.getStudent().getId())
                .filter(studentId -> studentCourseRequests.stream()
                        .noneMatch(studentCourseRequest -> Objects.equals(studentId, studentCourseRequest.id()))
                ).toList();
        if (!studentIdsToRemove.isEmpty()) {
            studentCourseRepository.deleteStudentCourseListByStudentIdsAndCourseId(studentIdsToRemove, courseId);
        }
    }

    private void addNewStudentCourse(List<StudentCourse> studentCourseList,
                                     List<StudentCourseRequest> studentCourseRequests,
                                     Long courseId) {
        List<StudentCourseRequest> studentCourseRequestToAdd = studentCourseRequests.stream()
                .filter(studentCourseRequest -> studentCourseList.stream()
                        .noneMatch(studentCourse -> Objects.equals(studentCourse.getStudent().getId(), studentCourseRequest.id()))
                )
                .toList();
        if (!studentCourseRequestToAdd.isEmpty()) {
            List<StudentCourseRecord> studentCourseRecordList = studentCourseMapper.mapToRecord(studentCourseRequestToAdd, courseId);
            studentCourseRepository.batchCreate(studentCourseRecordList);
        }
    }

    public CourseStudentResponse createStudentCourse(StudentCourseCreateRequest request) {
        Long courseId = Optional.ofNullable(request.courseId())
                .orElseGet(() -> courseService.getIdByTitle(request.courseTitle()));
        Long studentId = studentService.getIdByPhone(request.studentPhone());
        StudentCourseRecord studentCourseRecord = new StudentCourseRecord();
        studentCourseRecord.setStudentId(studentId);
        studentCourseRecord.setCourseId(courseId);
        studentCourseRepository.create(studentCourseRecord);
        return studentCourseMapper.mapToResponse(studentCourseRepository.findByStudentIdAndCourseId(studentId, courseId));
    }
}
