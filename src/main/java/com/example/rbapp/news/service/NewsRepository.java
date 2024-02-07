package com.example.rbapp.news.service;

import com.example.rbapp.jooq.codegen.tables.records.NewsRecord;
import com.example.rbapp.news.entity.News;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.NEWS;

@Repository
@RequiredArgsConstructor
public class NewsRepository {

    private final DSLContext dslContext;


    public List<NewsRecord> findAllNoneInternal() {
        return dslContext.selectFrom(NEWS)
                .where(NEWS.INTERNAL.isFalse())
                .fetchInto(NewsRecord.class);
    }

    public Optional<NewsRecord> findById(Long id) {
        return dslContext.selectFrom(NEWS)
                .where(NEWS.ID.eq(id))
                .fetchOptional();
    }

    public void create(NewsRecord news) {
        dslContext.insertInto(NEWS)
                .set(NEWS.DESCRIPTION, news.getDescription())
                .set(NEWS.PICTURE, news.getPicture())
                .set(NEWS.TITLE, news.getTitle())
                .set(NEWS.INTERNAL, news.getInternal())
                .set(NEWS.SHOW, news.getShow())
                .set(NEWS.SHORT_DESCRIPTION, news.getShortDescription())
                .execute();
    }

    public NewsRecord update(NewsRecord news) {
        dslContext.executeUpdate(news);
        return news;
    }

    public void deleteById(Long id) {
        dslContext.deleteFrom(NEWS).where(NEWS.ID.eq(id)).execute();
    }

    public List<NewsRecord> findAllInternal() {
        return dslContext.selectFrom(NEWS)
                .where(NEWS.INTERNAL.isTrue())
                .fetchInto(NewsRecord.class);
    }
}
