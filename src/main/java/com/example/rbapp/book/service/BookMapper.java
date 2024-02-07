package com.example.rbapp.book.service;

import com.example.rbapp.book.entity.Book;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.BookRecord;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface BookMapper {
    List<Book> mapToResponse(Collection<BookRecord> books);

    Book mapToResponse(BookRecord book);

    BookRecord mapToRecord(Book book);
}
