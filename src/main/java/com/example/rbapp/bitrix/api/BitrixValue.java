package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BitrixValue(@JsonProperty("VALUE") String value, @JsonProperty("VALUE_TYPE") String valueType) {
}
