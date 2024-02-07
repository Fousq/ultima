package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.jooq.codegen.tables.records.TeacherCourseRecord;
import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.service.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Repository
@RequiredArgsConstructor
public class TeacherRepositoryImpl implements TeacherRepository {

    private final DSLContext dslContext;

    @Override
    public Long create(TeacherRecord teacher) {
        return dslContext.insertInto(TEACHER)
                .set(TEACHER.NAME, teacher.getName())
                .set(TEACHER.EMAIL, teacher.getEmail())
                .set(TEACHER.PHONE, teacher.getPhone())
                .set(TEACHER.SURNAME, teacher.getSurname())
                .set(TEACHER.USER_ID, teacher.getUserId())
                .returningResult(TEACHER.ID)
                .fetchSingleInto(Long.class);
    }

    @Override
    public Optional<TeacherRecord> findById(Long id) {
        return dslContext.selectFrom(TEACHER).where(TEACHER.ID.eq(id)).fetchOptional();
    }

    @Override
    public List<TeacherRecord> findAll() {
        return dslContext.selectFrom(TEACHER).fetchInto(TeacherRecord.class);
    }

    @Override
    public List<TeacherRecord> findAllByCourseId(Long courseId) {
        return dslContext
                .select(TEACHER.fields()).from(TEACHER)
                .innerJoin(TEACHER_COURSE).on(TEACHER_COURSE.TEACHER_ID.eq(TEACHER.ID))
                .where(TEACHER_COURSE.COURSE_ID.eq(courseId))
                .fetchInto(TeacherRecord.class);
    }

    @Override
    public void createTeacherCourse(List<Long> teacherIds, Long courseId) {
        List<TeacherCourseRecord> teacherCourseRecords = teacherIds.stream().map(teacherId -> {
            TeacherCourseRecord teacherCourseRecord = new TeacherCourseRecord();
            teacherCourseRecord.setTeacherId(teacherId);
            teacherCourseRecord.setCourseId(courseId);
            return teacherCourseRecord;
        }).toList();

        dslContext.batchInsert(teacherCourseRecords).execute();
    }

    @Override
    public void deleteAllTeacherCourseByCourseId(Long courseId) {
        dslContext.deleteFrom(TEACHER_COURSE)
                .where(TEACHER_COURSE.COURSE_ID.eq(courseId))
                .execute();
    }

    @Override
    public void deleteTeacherCourseByTeacherIdsAndCourseId(List<Long> teacherIds, Long courseId) {
        dslContext.deleteFrom(TEACHER_COURSE)
                .where(TEACHER_COURSE.COURSE_ID.eq(courseId))
                .and(TEACHER_COURSE.TEACHER_ID.in(teacherIds))
                .execute();
    }

    @Override
    public void update(TeacherRecord teacherRecord) {
        dslContext.update(TEACHER)
                .set(TEACHER.NAME, teacherRecord.getName())
                .set(TEACHER.SURNAME, teacherRecord.getSurname())
                .set(TEACHER.PHONE, teacherRecord.getPhone())
                .set(TEACHER.EMAIL, teacherRecord.getEmail())
                .where(TEACHER.ID.eq(teacherRecord.getId()))
                .execute();
    }

    @Override
    public void updateBitrixIdById(Long id, Long bitrixId) {
        dslContext.update(TEACHER)
                .set(TEACHER.BITRIX_USER_ID, bitrixId)
                .where(TEACHER.ID.eq(id))
                .execute();
    }

    @Override
    public Optional<TeacherRecord> findByUserId(Long userId) {
        return dslContext.selectFrom(TEACHER)
                .where(TEACHER.USER_ID.eq(userId))
                .fetchOptionalInto(TeacherRecord.class);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(TEACHER)
                        .where(TEACHER.USER_ID.eq(userId))
        );
    }

    @Override
    public void deleteTeacherByUserId(Long userId) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            TeacherRecord teacher = context.selectFrom(TEACHER)
                    .where(TEACHER.USER_ID.eq(userId))
                    .fetchSingleInto(TeacherRecord.class);
            Long teacherId = teacher.getId();
            if (Objects.nonNull(teacher.getBitrixUserId())) {
                context.insertInto(DELETED_BITRIX_USER)
                        .set(DELETED_BITRIX_USER.NAME, teacher.getName())
                        .set(DELETED_BITRIX_USER.SURNAME, teacher.getSurname())
                        .set(DELETED_BITRIX_USER.EMAIL, teacher.getEmail())
                        .set(DELETED_BITRIX_USER.PHONE, teacher.getPhone())
                        .set(DELETED_BITRIX_USER.BITRIX_ID, teacher.getBitrixUserId())
                        .execute();
            }

            context.deleteFrom(TEACHER_COURSE)
                    .where(TEACHER_COURSE.TEACHER_ID.eq(teacherId))
                    .execute();
            context.deleteFrom(PAYMENT_RATE)
                    .where(PAYMENT_RATE.TEACHER_ID.eq(teacherId))
                    .execute();
            context.deleteFrom(TEACHER)
                    .where(TEACHER.ID.eq(teacherId))
                    .execute();
        });
    }
}
