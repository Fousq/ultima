package com.example.rbapp.book.contoller;

import com.example.rbapp.book.entity.Book;
import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.book.service.BookMapper;
import com.example.rbapp.book.service.BookRepository;
import com.example.rbapp.jooq.codegen.tables.records.BookRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @GetMapping
    public List<Book> bookList() {
        return bookMapper.mapToResponse(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Book book) {
        BookRecord bookRecord = bookMapper.mapToRecord(book);
        bookRepository.create(bookRecord);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public Book update(@RequestBody Book book) {
        BookRecord bookRecord = bookMapper.mapToRecord(book);
        return bookMapper.mapToResponse(bookRepository.update(bookRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
