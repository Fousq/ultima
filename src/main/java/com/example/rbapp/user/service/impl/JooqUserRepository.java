package com.example.rbapp.user.service.impl;

import com.example.rbapp.jooq.codegen.tables.records.AppUserRecord;
import com.example.rbapp.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.*;
import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class JooqUserRepository implements UserRepository {

    private final DSLContext dslContext;

    @Override
    public Optional<AppUserRecord> findByUsername(String username) {
        return dslContext.selectFrom(APP_USER)
                .where(APP_USER.USERNAME.eq(username))
                .fetchOptional();
    }

    @Override
    public Long create(AppUserRecord user) {
        return dslContext.insertInto(APP_USER)
                .set(APP_USER.USERNAME, user.getUsername())
                .set(APP_USER.PASSWORD, user.getPassword())
                .set(APP_USER.GRANT_TYPE, user.getGrantType())
                .returningResult(APP_USER.ID)
                .fetchSingleInto(Long.class);
    }

    @Override
    public Optional<AppUserRecord> findByUsernameAndPassword(String username, String password) {
        return dslContext.selectFrom(APP_USER)
                .where(APP_USER.USERNAME.eq(username))
                .and(APP_USER.PASSWORD.eq(password))
                .fetchOptional();
    }

    @Override
    public void updatePassword(AppUserRecord user) {
        dslContext.update(APP_USER)
                .set(APP_USER.PASSWORD, user.getPassword())
                .where(APP_USER.ID.eq(user.getId()))
                .execute();
    }

    @Override
    public String findFullNameByUserId(Long userId) {
        var studentFullNameConcat = concat(STUDENT.SURNAME, val(" "), STUDENT.NAME,
                when(STUDENT.MIDDLE_NAME.isNotNull(), concat(val(" "), STUDENT.MIDDLE_NAME)).otherwise("")
        );
        var teacherFullNameConcat = concat(TEACHER.SURNAME, val(" "), TEACHER.NAME);
        var headTeacherFullNameConcat = concat(HEAD_TEACHER.SURNAME, val(" "), HEAD_TEACHER.NAME);
        return dslContext.select(
                when(STUDENT.ID.isNotNull(), studentFullNameConcat)
                        .when(TEACHER.ID.isNotNull(), teacherFullNameConcat)
                        .when(HEAD_TEACHER.ID.isNotNull(), headTeacherFullNameConcat)
                        .otherwise("")
                )
                .from(APP_USER)
                .leftJoin(STUDENT).on(STUDENT.USER_ID.eq(APP_USER.ID))
                .leftJoin(TEACHER).on(TEACHER.USER_ID.eq(APP_USER.ID))
                .leftJoin(HEAD_TEACHER).on(HEAD_TEACHER.USER_ID.eq(APP_USER.ID))
                .where(APP_USER.ID.eq(userId))
                .fetchSingleInto(String.class);
    }

    @Override
    public void deleteById(Long id) {
        dslContext.deleteFrom(APP_USER)
                .where(APP_USER.ID.eq(id))
                .execute();
    }

    @Override
    public List<Long> findAllIdByRoles(List<String> roles) {
        return dslContext.select(APP_USER.ID).from(APP_USER)
                .where(APP_USER.GRANT_TYPE.in(roles))
                .fetchInto(Long.class);
    }
}
