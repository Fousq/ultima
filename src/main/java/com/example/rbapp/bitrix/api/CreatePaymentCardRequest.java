package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePaymentCardRequest(@JsonProperty("entityTypeId") Long id,
                                       @JsonProperty("fields") BitrixCreatePaymentCardFields fields) {
}
