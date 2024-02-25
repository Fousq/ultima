package com.example.rbapp.student.service;

import com.example.rbapp.jooq.codegen.tables.records.StudentRecord;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final DSLContext dslContext;


    public Long create(StudentRecord student) {
        return dslContext.insertInto(STUDENT)
                .set(STUDENT.NAME, student.getName())
                .set(STUDENT.EMAIL, student.getEmail())
                .set(STUDENT.PHONE, student.getPhone())
                .set(STUDENT.SURNAME, student.getSurname())
                .set(STUDENT.USER_ID, student.getUserId())
                .set(STUDENT.BIRTHDAY, student.getBirthday())
                .returningResult(STUDENT.ID)
                .fetchSingleInto(Long.class);
    }

    public Optional<StudentRecord> findById(Long id) {
        return dslContext.selectFrom(STUDENT).where(STUDENT.ID.eq(id)).fetchOptional();
    }

    public List<StudentRecord> findAll() {
        return dslContext.selectFrom(STUDENT).fetchInto(StudentRecord.class);
    }

    public List<StudentRecord> findAllByIds(List<Long> studentIds) {
        return dslContext.selectFrom(STUDENT).where(STUDENT.ID.in(studentIds)).fetchInto(StudentRecord.class);
    }

    public List<StudentRecord> findAllByCourseSubjectId(Long courseSubjectId) {
        return dslContext
                .select(STUDENT.fields()).from(STUDENT)
                .innerJoin(STUDENT_COURSE_SUBJECT).on(STUDENT_COURSE_SUBJECT.STUDENT_ID.eq(STUDENT.ID))
                .where(STUDENT_COURSE_SUBJECT.COURSE_SUBJECT_ID.eq(courseSubjectId))
                .fetchInto(StudentRecord.class);
    }

    public Optional<Long> findIdByPhone(String phone) {
        return dslContext.select(STUDENT.ID).from(STUDENT)
                .where(STUDENT.PHONE.eq(phone))
                .fetchOptionalInto(Long.class);
    }

    public List<StudentRecord> findAllByCourseId(Long courseId) {
        return dslContext.select(STUDENT.fields()).from(STUDENT)
                .innerJoin(STUDENT_COURSE).on(STUDENT_COURSE.STUDENT_ID.eq(STUDENT.ID))
                .where(STUDENT_COURSE.COURSE_ID.eq(courseId))
                .fetchInto(StudentRecord.class);
    }

    public List<Long> findAllIdsByCourseSubjectId(Long courseSubjectId) {
        return dslContext.select(STUDENT_COURSE_SUBJECT.STUDENT_ID)
                .from(STUDENT_COURSE_SUBJECT)
                .where(STUDENT_COURSE_SUBJECT.COURSE_SUBJECT_ID.eq(courseSubjectId))
                .fetchInto(Long.class);
    }

    public void update(StudentRecord studentRecord) {
        dslContext.update(STUDENT)
                .set(STUDENT.NAME, studentRecord.getName())
                .set(STUDENT.SURNAME, studentRecord.getSurname())
                .set(STUDENT.WISHES, studentRecord.getWishes())
                .set(STUDENT.PHONE, studentRecord.getPhone())
                .set(STUDENT.STUDY_GOAL, studentRecord.getStudyGoal())
                .set(STUDENT.MIDDLE_NAME, studentRecord.getMiddleName())
                .set(STUDENT.CITY, studentRecord.getCity())
                .set(STUDENT.EMAIL, studentRecord.getEmail())
                .set(STUDENT.BIRTHDAY, studentRecord.getBirthday())
                .where(STUDENT.ID.eq(studentRecord.getId()))
                .execute();
    }

    public Optional<Long> findIdByEmail(String email) {
        return dslContext.select(STUDENT.ID).from(STUDENT)
                .where(STUDENT.EMAIL.eq(email))
                .fetchOptionalInto(Long.class);
    }

    public void updateBitrixIdById(Long bitrixId, Long id) {
        dslContext.update(STUDENT)
                .set(STUDENT.BITRIX_CONTACT_ID, bitrixId)
                .where(STUDENT.ID.eq(id))
                .execute();
    }

    public Optional<StudentRecord> findByUserId(Long userId) {
        return dslContext.selectFrom(STUDENT)
                .where(STUDENT.USER_ID.eq(userId))
                .fetchOptionalInto(StudentRecord.class);
    }

    public void deleteById(Long id) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            deleteById(context, id);
        });
    }

    public boolean existsByUserId(Long userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(STUDENT)
                        .where(STUDENT.USER_ID.eq(userId))
        );
    }

    public void deleteByUserId(Long userId) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            StudentRecord student = context.selectFrom(STUDENT)
                    .where(STUDENT.USER_ID.eq(userId))
                    .fetchSingleInto(StudentRecord.class);

            if (Objects.nonNull(student.getBitrixContactId())) {
                context.insertInto(DELETED_BITRIX_USER)
                        .set(DELETED_BITRIX_USER.NAME, student.getName())
                        .set(DELETED_BITRIX_USER.SURNAME, student.getSurname())
                        .set(DELETED_BITRIX_USER.EMAIL, student.getEmail())
                        .set(DELETED_BITRIX_USER.PHONE, student.getPhone())
                        .set(DELETED_BITRIX_USER.BITRIX_ID, student.getBitrixContactId())
                        .execute();
            }

            deleteById(context, student.getId());
        });
    }

    private void deleteById(DSLContext transactionContext, Long id) {
        transactionContext.deleteFrom(STUDENT_COURSE_SUBJECT)
                .where(STUDENT_COURSE_SUBJECT.STUDENT_ID.eq(id))
                .execute();
        transactionContext.deleteFrom(STUDENT_COURSE)
                .where(STUDENT_COURSE.STUDENT_ID.eq(id))
                .execute();
        transactionContext.deleteFrom(STUDENT_HOMEWORK)
                .where(STUDENT_HOMEWORK.STUDENT_ID.eq(id))
                .execute();
        transactionContext.deleteFrom(STUDENT_VIDEO_CLASS)
                .where(STUDENT_VIDEO_CLASS.STUDENT_ID.eq(id))
                .execute();
        transactionContext.deleteFrom(TIME_PACKAGE)
                .where(TIME_PACKAGE.STUDENT_ID.eq(id))
                .execute();
        transactionContext.deleteFrom(STUDENT)
                .where(STUDENT.ID.eq(id))
                .execute();
    }
}
