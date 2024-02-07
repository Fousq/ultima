package com.example.rbapp.book.service;

import com.example.rbapp.book.entity.Book;
import com.example.rbapp.jooq.codegen.tables.records.BookRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.BOOK;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final DSLContext dslContext;


    public List<BookRecord> findAll() {
        return dslContext.selectFrom(BOOK).fetchInto(BookRecord.class);
    }

    public Optional<BookRecord> findById(Long id) {
        return dslContext.selectFrom(BOOK)
                .where(BOOK.ID.eq(id))
                .fetchOptional();
    }

    public void create(BookRecord book) {
        dslContext.insertInto(BOOK)
                .set(BOOK.TITLE, book.getTitle())
                .execute();
    }

    public BookRecord update(BookRecord bookRecord) {
        dslContext.executeUpdate(bookRecord);
        return bookRecord;
    }

    public void deleteById(Long id) {
        dslContext.deleteFrom(BOOK).where(BOOK.ID.eq(id)).execute();
    }
}
