package com.example.rbapp.currency;

import com.example.rbapp.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public Long getIdByCode(String code) {
        return currencyRepository.findIdByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency id not found by code"));
    }

    public String getCodeById(Long id) {
        return currencyRepository.findCodeById(id)
                .orElseThrow(() -> new NotFoundException("Currency code not found by id"));
    }
}
