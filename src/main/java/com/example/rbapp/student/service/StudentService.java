package com.example.rbapp.student.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.course.service.CourseService;
import com.example.rbapp.jooq.codegen.tables.records.CourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.StudentRecord;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.student.controller.api.StudentUpdateRequest;
import com.example.rbapp.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CourseService courseService;

    public List<StudentResponse> getAllStudents(Boolean includeCourseParticipation) {
        List<StudentRecord> studentRecords = studentRepository.findAll();
        if (includeCourseParticipation) {
            return studentRecords.stream().map(studentRecord -> {
                List<CourseRecord> courses = courseService.getStudentCourses(studentRecord.getId());
                return studentMapper.mapToResponse(studentRecord, courses);
            }).toList();
        }
        return studentMapper.mapToResponse(studentRecords);
    }


    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::mapToEntity)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public Long getIdByPhone(String phone) {
        return studentRepository.findIdByPhone(phone)
                .orElseThrow(() -> new NotFoundException("Student not found by phone"));
    }

    public List<Student> getStudentsByCourseId(Long courseId) {
        return studentMapper.mapToEntity(studentRepository.findAllByCourseId(courseId));
    }

    public StudentResponse update(Long id, StudentUpdateRequest request) {
        StudentRecord studentRecord = studentMapper.mapUpdateRequestToRecord(request, id);
        studentRepository.update(studentRecord);
        return studentMapper.mapEntityToResponse(getStudent(id));
    }

    public Long create(Student student) {
        StudentRecord studentRecord = studentMapper.mapEntityToRecord(student);
        return studentRepository.create(studentRecord);
    }

    public Long getIdByEmail(String email) {
        return studentRepository.findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException("Student id not found by email"));
    }

    public void updateBitrixId(Student student, Long bitrixId) {
        studentRepository.updateBitrixIdById(bitrixId, student.getId());
    }

    public StudentResponse getStudentResponseByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .map(studentMapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Student not found by user id"));
    }

    public Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .map(studentMapper::mapToEntity)
                .orElseThrow(() -> new NotFoundException("Student not found by user id"));
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsByUserId(Long userId) {
        return studentRepository.existsByUserId(userId);
    }

    public void deleteByUserId(Long userId) {
        studentRepository.deleteByUserId(userId);
    }

    public void updateEntity(Student student) {
        StudentRecord studentRecord = studentMapper.mapEntityToRecord(student);
        studentRepository.update(studentRecord);
    }
}
