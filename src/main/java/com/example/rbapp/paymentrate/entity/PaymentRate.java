package com.example.rbapp.paymentrate.entity;

import lombok.Data;

@Data
public class PaymentRate {

    private Long id;

    private Long currencyId;

    private String type;

    private Double amount;
}
