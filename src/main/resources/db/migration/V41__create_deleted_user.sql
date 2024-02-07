CREATE TABLE IF NOT EXISTS deleted_bitrix_user(
    id bigserial PRIMARY KEY,
    name VARCHAR,
    surname VARCHAR,
    phone VARCHAR,
    email VARCHAR,
    bitrix_id bigint not null
);