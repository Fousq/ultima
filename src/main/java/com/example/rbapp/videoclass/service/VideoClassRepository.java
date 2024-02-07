package com.example.rbapp.videoclass.service;

import com.example.rbapp.jooq.codegen.tables.records.VideoClassRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.VIDEO_CLASS;

@Repository
@RequiredArgsConstructor
public class VideoClassRepository {

    private final DSLContext dslContext;


    public List<VideoClassRecord> findAll() {
        return dslContext.selectFrom(VIDEO_CLASS).fetchInto(VideoClassRecord.class);
    }

    public Optional<VideoClassRecord> findById(Long id) {
        return dslContext.selectFrom(VIDEO_CLASS)
                .where(VIDEO_CLASS.ID.eq(id))
                .fetchOptional();
    }

    public void create(VideoClassRecord videoClass) {
        dslContext.insertInto(VIDEO_CLASS)
                .set(VIDEO_CLASS.TITLE, videoClass.getTitle())
                .set(VIDEO_CLASS.LINK, videoClass.getLink())
                .execute();
    }

    public VideoClassRecord update(VideoClassRecord videoClassRecord) {
        dslContext.executeUpdate(videoClassRecord);
        return videoClassRecord;
    }

    public void deleteById(Long id) {
        dslContext.deleteFrom(VIDEO_CLASS).where(VIDEO_CLASS.ID.eq(id)).execute();
    }
}
