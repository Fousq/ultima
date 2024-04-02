package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateContactWithTypeRequest(@JsonProperty("fields") BitrixCreateContactWithTypeFields fields) {
}
