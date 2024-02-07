package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateClientResponse(@JsonProperty("result") Long clientId) {
}
