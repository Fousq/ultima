package com.example.rbapp.studentvideoclass.service;

import com.example.rbapp.jooq.codegen.tables.records.StudentCourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentVideoClassRecord;
import com.example.rbapp.student.controller.api.StudentCourseRequest;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.student.service.StudentService;
import com.example.rbapp.studentcourse.entity.StudentCourse;
import com.example.rbapp.studentvideoclass.api.StudentVideoClassUpdateRequest;
import com.example.rbapp.studentvideoclass.entity.StudentVideoClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentVideoClassService {

    private final StudentVideoClassRepository studentVideoClassRepository;
    private final StudentService studentService;
    private final StudentVideoClassMapper studentVideoClassMapper;

    public List<StudentVideoClass> getByVideoClassId(Long videoClassId) {
        var studentVideoClassRecords = studentVideoClassRepository.findAllByVideoClassId(videoClassId);
        return studentVideoClassRecords.stream()
                .map(studentVideoClassRecord -> {
                    StudentVideoClass studentVideoClass = studentVideoClassMapper.mapToEntity(studentVideoClassRecord);
                    Student student = studentService.getStudent(studentVideoClassRecord.getStudentId());
                    studentVideoClass.setStudent(student);
                    return studentVideoClass;
                })
                .toList();
    }

    public void updateList(List<StudentVideoClassUpdateRequest> studentVideoClassUpdateRequests, Long videoClassId) {
        var studentVideoClassList = getByVideoClassId(videoClassId);
        update(studentVideoClassList, studentVideoClassUpdateRequests);
        removeUnlinkedStudent(studentVideoClassList, studentVideoClassUpdateRequests, videoClassId);
        addNewStudentVideoClass(studentVideoClassList, studentVideoClassUpdateRequests, videoClassId);
    }


    private void update(List<StudentVideoClass> studentVideoClassList,
                        List<StudentVideoClassUpdateRequest> studentVideoClassUpdateRequests) {
        studentVideoClassList.forEach(studentVideoClass ->
                studentVideoClassUpdateRequests.stream()
                        .filter(studentVideoClassRequest ->
                                Objects.equals(studentVideoClass.getStudent().getId(), studentVideoClassRequest.id())
                        )
                        .findFirst()
                        .ifPresent(studentCourseRequest ->
                                studentVideoClass.setVisited(studentCourseRequest.visited())
                        ));
        List<StudentVideoClassRecord> studentCourseRecordList = studentVideoClassMapper.mapToRecord(studentVideoClassList);
        studentVideoClassRepository.batchUpdate(studentCourseRecordList);
    }

    private void removeUnlinkedStudent(List<StudentVideoClass> studentVideoClassList,
                                       List<StudentVideoClassUpdateRequest> studentVideoClassUpdateRequests,
                                       Long videoClassId) {
        List<Long> studentIdsToRemove = studentVideoClassList.stream()
                .map(studentVideoClass -> studentVideoClass.getStudent().getId())
                .filter(studentId -> studentVideoClassUpdateRequests.stream()
                        .noneMatch(studentVideoClassUpdateRequest ->
                                Objects.equals(studentId, studentVideoClassUpdateRequest.id()))
                ).toList();
        if (!studentIdsToRemove.isEmpty()) {
            studentVideoClassRepository.deleteAll(studentIdsToRemove, videoClassId);
        }
    }

    private void addNewStudentVideoClass(List<StudentVideoClass> studentVideoClassList,
                                     List<StudentVideoClassUpdateRequest> studentVideoClassUpdateRequests,
                                     Long videoClassId) {
        List<StudentVideoClassUpdateRequest> studentVideoClassToAdd = studentVideoClassUpdateRequests.stream()
                .filter(studentVideoClassRequest -> studentVideoClassList.stream()
                        .map(studentVideoClass -> studentVideoClass.getStudent().getId())
                        .noneMatch(studentId -> Objects.equals(studentId, studentVideoClassRequest.id()))
                )
                .toList();
        if (!studentVideoClassToAdd.isEmpty()) {
            List<StudentVideoClassRecord> studentVideoClassRecords =
                    studentVideoClassMapper.mapUpdateRequestToRecord(studentVideoClassToAdd);
            studentVideoClassRepository.batchCreate(studentVideoClassRecords, videoClassId);
        }
    }
}
