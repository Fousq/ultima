package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserRequest(@JsonProperty("UF_DEPARTMENT") Long departmentId,
                                @JsonProperty("EMAIL") String email,
                                @JsonProperty("NAME") String name,
                                @JsonProperty("LAST_NAME") String lastName,
                                @JsonProperty("WORK_POSITION") String position,
                                @JsonProperty("PERSONAL_PHONE") String phone) {
}
