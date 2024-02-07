INSERT INTO currency(id, code)
    VALUES
    (1, 'KZT'),
    (2, 'RUB'),
    (3, 'USD'),
    (4, 'EUR'),
    (5, 'BYN')
    ON CONFLICT (id) DO UPDATE
    SET code = EXCLUDED.code;