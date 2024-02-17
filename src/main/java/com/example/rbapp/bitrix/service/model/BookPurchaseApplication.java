package com.example.rbapp.bitrix.service.model;

import com.example.rbapp.jooq.codegen.tables.records.BookRecord;

public record BookPurchaseApplication(String name, String phone, String city, BookRecord book, Integer amount) {
}
