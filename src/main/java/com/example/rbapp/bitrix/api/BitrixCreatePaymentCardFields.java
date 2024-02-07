package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record BitrixCreatePaymentCardFields(@JsonProperty("title") String title,
                                            @JsonProperty("categoryId") Long categoryId,
                                            @JsonProperty("currencyId") String currency,
                                            @JsonProperty("opportunity") BigDecimal total,
                                            @JsonProperty("ufCrm5SummaOplaty") BigDecimal confirmedTotal,
                                            @JsonProperty("ufCrm5FioPrepod") String fullName) {
}
