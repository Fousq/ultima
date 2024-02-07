package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BitrixCreateDealFields(@JsonProperty("TITLE") String title,
                                     @JsonProperty("CATEGORY_ID") Long categoryId,
                                     @JsonProperty("CONTACT_ID") Long contactId) {
}
