package com.example.rbapp.course.service;

import com.example.rbapp.course.controller.api.CourseSaveRequest;
import com.example.rbapp.course.entity.Course;
import com.example.rbapp.coursesubject.entity.CourseSubject;
import com.example.rbapp.coursesubject.service.CourseSubjectMapper;
import com.example.rbapp.coursesubject.service.CourseSubjectService;
import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.student.service.StudentService;
import com.example.rbapp.studentcourse.service.StudentCourseService;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherMapper;
import com.example.rbapp.teacher.service.TeacherRepository;
import com.example.rbapp.teacher.service.impl.TeacherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseDetailsService {

    private final StudentCourseService studentCourseService;
    private final TeacherRepository teacherRepository;
    private final CourseSubjectService courseSubjectService;
    private final TeacherMapper teacherMapper;
    private final TeacherServiceImpl teacherService;
    private final CourseSubjectMapper courseSubjectMapper;
    private final CourseService courseService;
    private final StudentService studentService;

    public List<Course> getCourseList() {
        List<Course> courses = courseService.getCourseList();
        courses.forEach(this::fetchDetails);
        return courses;
    }

    public Course getCourse(Long id) {
        return fetchDetails(courseService.getCourse(id));
    }

    public Course fetchDetails(Course course) {
        Long courseId = course.getId();
        List<Student> studentCourseList = studentService.getStudentsByCourseId(courseId);
        course.setStudentList(studentCourseList);
        List<TeacherRecord> teacherRecordList = teacherRepository.findAllByCourseId(courseId);
        List<Teacher> teacherList = teacherMapper.mapRecordToEntity(teacherRecordList);
        course.setTeacherList(teacherList);
        List<CourseSubject> courseSubjects = courseSubjectService.getCourseSubjectsByCourseId(courseId);
        course.setCourseSubjectList(courseSubjects);
        return course;
    }

    @Transactional
    public void create(CourseSaveRequest course) {
        Long courseId = courseService.create(course);
        createDetails(course, courseId);
    }

    public void createDetails(CourseSaveRequest course, Long courseId) {
        if (course.studentList() != null && !course.studentList().isEmpty()) {
            studentCourseService.addStudentsToCourse(course.studentList(), courseId);
        }
        if (course.courseSubjects() != null && !course.courseSubjects().isEmpty()) {
            List<CourseSubject> courseSubjects = courseSubjectMapper.mapUpdateRequestToEntity(course.courseSubjects());
            courseSubjectService.create(courseSubjects, courseId);
        }
        if (course.teacherList() != null && !course.teacherList().isEmpty()) {
            // TODO move to service
            List<Long> teacherIds = course.teacherList().stream().map(Teacher::getId).toList();
            teacherRepository.createTeacherCourse(teacherIds, courseId);
        }
    }

    @Transactional
    public List<Course> update(List<CourseSaveRequest> courseList) {
        return courseList.stream().map(course -> update(course, course.id())).collect(Collectors.toList());
    }

    public Course update(CourseSaveRequest courseSaveRequest, Long id) {
        courseService.update(courseSaveRequest, id);
        updateDetails(courseSaveRequest, id);

        return getCourse(id);
    }

    public void updateDetails(CourseSaveRequest courseSaveRequest, Long courseId) {
        if (courseSaveRequest.studentList().isEmpty()) {
            studentCourseService.deleteStudentsFromCourse(courseId);
        } else {
            studentCourseService.updateStudentCourseList(courseSaveRequest.studentList(), courseId);
        }

        if (courseSaveRequest.teacherList().isEmpty()) {
            teacherRepository.deleteAllTeacherCourseByCourseId(courseId);
        } else {
            teacherService.updateTeacherCourseList(courseSaveRequest.teacherList(), courseId);
        }

        if (courseSaveRequest.courseSubjects().isEmpty()) {
            courseSubjectService.deleteFromCourse(courseId);
        } else {
            courseSubjectService.update(courseSaveRequest.courseSubjects(), courseId);
        }
    }
}
