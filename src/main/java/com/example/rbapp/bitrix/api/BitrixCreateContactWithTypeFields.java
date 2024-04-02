package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BitrixCreateContactWithTypeFields(@JsonProperty("TITLE") String title,
                                                @JsonProperty("TYPE_ID") Integer typeId,
                                                @JsonProperty("NAME") String name,
                                                @JsonProperty("EMAIL") List<BitrixValue> email,
                                                @JsonProperty("PHONE") List<BitrixValue> phone) {
}
