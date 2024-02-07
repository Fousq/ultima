package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BitrixResponse(@JsonProperty("result") Long id) {
}
