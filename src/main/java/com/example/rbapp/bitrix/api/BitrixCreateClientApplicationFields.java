package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BitrixCreateClientApplicationFields(@JsonProperty("TITLE") String title,
                                                  @JsonProperty("NAME") String name,
                                                  @JsonProperty("PHONE") List<BitrixValue> phone) {
}
