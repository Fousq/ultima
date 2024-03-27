package com.example.rbapp.currency;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.rbapp.jooq.codegen.Tables.CURRENCY;

@Repository
@RequiredArgsConstructor
public class CurrencyRepository {

    private final DSLContext dslContext;

    public Optional<Long> findIdByCode(String code) {
        return dslContext.select(CURRENCY.ID).from(CURRENCY)
                .where(CURRENCY.CODE.eq(code))
                .fetchOptionalInto(Long.class);
    }

    public Optional<String> findCodeById(Long id) {
        return dslContext.select(CURRENCY.CODE).from(CURRENCY)
                .where(CURRENCY.ID.eq(id))
                .fetchOptionalInto(String.class);
    }
}
