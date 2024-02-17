package com.example.rbapp.book.contoller.api;

public record SendPurchaseApplicationRequest(String name, String phone, String city, Long bookId, Integer amount) {
}
