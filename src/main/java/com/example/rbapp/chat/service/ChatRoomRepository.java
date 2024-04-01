package com.example.rbapp.chat.service;

import com.example.rbapp.jooq.codegen.tables.records.ChatRoomRecord;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomUserRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.CHAT_ROOM;
import static com.example.rbapp.jooq.codegen.Tables.CHAT_ROOM_USER;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final DSLContext dslContext;

    public List<ChatRoomRecord> findAllByUserId(Long userId) {
        return dslContext.select(CHAT_ROOM.ID, CHAT_ROOM_USER.TITLE).from(CHAT_ROOM)
                .innerJoin(CHAT_ROOM_USER).on(CHAT_ROOM_USER.CHAT_ROOM_ID.eq(CHAT_ROOM.ID))
                .where(CHAT_ROOM_USER.USER_ID.eq(userId))
                .fetchInto(ChatRoomRecord.class);
    }

    public Long createRoom(String title) {
        return dslContext.insertInto(CHAT_ROOM)
                .set(CHAT_ROOM.TITLE, title)
                .returningResult(CHAT_ROOM.ID)
                .fetchSingleInto(Long.class);
    }

    public void addUserToRoom(String title, Long chatRoomId, Long userId) {
        dslContext.insertInto(CHAT_ROOM_USER)
                .set(CHAT_ROOM_USER.CHAT_ROOM_ID, chatRoomId)
                .set(CHAT_ROOM_USER.TITLE, title)
                .set(CHAT_ROOM_USER.USER_ID, userId)
                .execute();
    }

    public Optional<ChatRoomRecord> findByIdAndUserId(Long chatRoomId, Long userId) {
        return dslContext.select(CHAT_ROOM.ID, CHAT_ROOM_USER.TITLE).from(CHAT_ROOM)
                .innerJoin(CHAT_ROOM_USER).on(CHAT_ROOM_USER.CHAT_ROOM_ID.eq(CHAT_ROOM.ID))
                .where(CHAT_ROOM.ID.eq(chatRoomId))
                .and(CHAT_ROOM_USER.USER_ID.eq(userId))
                .fetchOptionalInto(ChatRoomRecord.class);
    }

    public boolean isExistsById(Long id) {
        return dslContext.fetchExists(
                dslContext.selectFrom(CHAT_ROOM).where(CHAT_ROOM.ID.eq(id))
        );
    }

    public Map<Long, Long> findRoomReceiverIdByIdsAndNotUserId(List<Long> ids, Long userId) {
        return dslContext.select(CHAT_ROOM.ID, CHAT_ROOM_USER.USER_ID)
                .from(CHAT_ROOM)
                .innerJoin(CHAT_ROOM_USER).on(CHAT_ROOM_USER.CHAT_ROOM_ID.eq(CHAT_ROOM.ID))
                .where(CHAT_ROOM_USER.USER_ID.notEqual(userId))
                .and(CHAT_ROOM.ID.in(ids))
                .fetchMap(CHAT_ROOM.ID, CHAT_ROOM_USER.USER_ID);
    }

    public boolean existsByUserId(Long userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(CHAT_ROOM_USER)
                        .where(CHAT_ROOM_USER.USER_ID.eq(userId))
        );
    }

    public void deleteAllByUserId(Long userId) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            List<Long> chatRoomIds = context.select(CHAT_ROOM_USER.CHAT_ROOM_ID)
                    .from(CHAT_ROOM_USER)
                    .where(CHAT_ROOM_USER.USER_ID.eq(userId))
                    .fetchInto(Long.class);
            context.deleteFrom(CHAT_ROOM_USER)
                    .where(CHAT_ROOM_USER.CHAT_ROOM_ID.in(chatRoomIds))
                    .execute();
            context.deleteFrom(CHAT_ROOM)
                    .where(CHAT_ROOM.ID.in(chatRoomIds))
                    .execute();
        });
    }
}
