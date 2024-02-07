package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientListRequest(@JsonProperty("PHONE") String phone) {
}
