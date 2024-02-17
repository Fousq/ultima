package com.example.rbapp.book.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.bitrix.client.BitrixClient;
import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.bitrix.service.model.BookPurchaseApplication;
import com.example.rbapp.book.contoller.api.SendPurchaseApplicationRequest;
import com.example.rbapp.jooq.codegen.tables.records.BookRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BitrixService bitrixService;

    public void processSendPurchaseApplication(SendPurchaseApplicationRequest request) {
        BookRecord book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("No such book"));
        BookPurchaseApplication bookPurchaseApplication = new BookPurchaseApplication(request.name(), request.phone(),
                request.city(), book, request.amount());
        bitrixService.createBookPurchaseApplication(bookPurchaseApplication);
    }
}
