package com.example.rbapp.supervisor.service;

import com.example.rbapp.jooq.codegen.tables.records.SupervisorRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.DELETED_BITRIX_USER;
import static com.example.rbapp.jooq.codegen.Tables.SUPERVISOR;

@Repository
@RequiredArgsConstructor
public class SupervisorRepository {

    private final DSLContext dslContext;

    public Long create(SupervisorRecord supervisorRecord) {
        return dslContext.insertInto(SUPERVISOR)
                .set(SUPERVISOR.NAME, supervisorRecord.getName())
                .set(SUPERVISOR.SURNAME, supervisorRecord.getSurname())
                .set(SUPERVISOR.PHONE, supervisorRecord.getPhone())
                .set(SUPERVISOR.EMAIL, supervisorRecord.getEmail())
                .set(SUPERVISOR.USER_ID, supervisorRecord.getUserId())
                .returningResult(SUPERVISOR.ID)
                .fetchSingleInto(Long.class);
    }

    public void updateBitrixIdBySupervisorId(Long bitrixId, Long supervisorId) {
        dslContext.update(SUPERVISOR)
                .set(SUPERVISOR.BITRIX_USER_ID, bitrixId)
                .where(SUPERVISOR.ID.eq(supervisorId))
                .execute();
    }

    public List<SupervisorRecord> findAll() {
        return dslContext.selectFrom(SUPERVISOR)
                .fetchInto(SupervisorRecord.class);
    }

    public Optional<SupervisorRecord> findById(Long id) {
        return dslContext.selectFrom(SUPERVISOR)
                .where(SUPERVISOR.ID.eq(id))
                .fetchOptionalInto(SupervisorRecord.class);
    }

    public boolean existsByUserId(Long userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(SUPERVISOR)
                        .where(SUPERVISOR.USER_ID.eq(userId))
        );
    }

    public void deleteByUserId(Long userId) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            SupervisorRecord supervisor = context.selectFrom(SUPERVISOR)
                    .where(SUPERVISOR.USER_ID.eq(userId))
                    .fetchSingleInto(SupervisorRecord.class);
            if (Objects.nonNull(supervisor.getBitrixUserId())) {
                context.insertInto(DELETED_BITRIX_USER)
                        .set(DELETED_BITRIX_USER.NAME, supervisor.getName())
                        .set(DELETED_BITRIX_USER.SURNAME, supervisor.getSurname())
                        .set(DELETED_BITRIX_USER.PHONE, supervisor.getPhone())
                        .set(DELETED_BITRIX_USER.EMAIL, supervisor.getEmail())
                        .set(DELETED_BITRIX_USER.BITRIX_ID, supervisor.getBitrixUserId())
                        .execute();
            }

            context.deleteFrom(SUPERVISOR)
                    .where(SUPERVISOR.USER_ID.eq(userId))
                    .execute();
        });
    }
}
