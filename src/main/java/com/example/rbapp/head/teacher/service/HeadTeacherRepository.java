package com.example.rbapp.head.teacher.service;

import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.jooq.codegen.tables.records.HeadTeacherRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.DELETED_BITRIX_USER;
import static com.example.rbapp.jooq.codegen.Tables.HEAD_TEACHER;

@Repository
@RequiredArgsConstructor
public class HeadTeacherRepository {

    private final DSLContext dslContext;


    public Long create(HeadTeacherRecord headTeacher) {
        return dslContext.insertInto(HEAD_TEACHER)
                .set(HEAD_TEACHER.EMAIL, headTeacher.getEmail())
                .set(HEAD_TEACHER.NAME, headTeacher.getName())
                .set(HEAD_TEACHER.PHONE, headTeacher.getPhone())
                .set(HEAD_TEACHER.SURNAME, headTeacher.getSurname())
                .set(HEAD_TEACHER.USER_ID, headTeacher.getUserId())
                .returningResult(HEAD_TEACHER.ID)
                .fetchSingleInto(Long.class);
    }

    public List<HeadTeacherRecord> findAll() {
        return dslContext.selectFrom(HEAD_TEACHER).fetchInto(HeadTeacherRecord.class);
    }

    public Optional<HeadTeacherRecord> findById(Long id) {
        return dslContext.selectFrom(HEAD_TEACHER)
                .where(HEAD_TEACHER.ID.eq(id))
                .fetchOptional();
    }

    public void updateBitrixIdById(Long bitrixId, Long id) {
        dslContext.update(HEAD_TEACHER)
                .set(HEAD_TEACHER.BITRIX_USER_ID, bitrixId)
                .where(HEAD_TEACHER.ID.eq(id))
                .execute();
    }

    public boolean existsByUserId(Long userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(HEAD_TEACHER)
                        .where(HEAD_TEACHER.USER_ID.eq(userId))
        );
    }

    public void deleteByUserId(Long userId) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            HeadTeacherRecord headTeacher = dslContext.selectFrom(HEAD_TEACHER)
                    .where(HEAD_TEACHER.USER_ID.eq(userId))
                    .fetchSingleInto(HeadTeacherRecord.class);
            if (Objects.nonNull(headTeacher.getBitrixUserId())) {
                context.insertInto(DELETED_BITRIX_USER)
                        .set(DELETED_BITRIX_USER.NAME, headTeacher.getName())
                        .set(DELETED_BITRIX_USER.SURNAME, headTeacher.getSurname())
                        .set(DELETED_BITRIX_USER.EMAIL, headTeacher.getEmail())
                        .set(DELETED_BITRIX_USER.PHONE, headTeacher.getPhone())
                        .set(DELETED_BITRIX_USER.BITRIX_ID, headTeacher.getBitrixUserId())
                        .execute();
            }

            context.deleteFrom(HEAD_TEACHER)
                    .where(HEAD_TEACHER.ID.eq(headTeacher.getId()))
                    .execute();
        });
    }
}
