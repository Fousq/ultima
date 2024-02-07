CREATE TABLE IF NOT EXISTS news(
    id bigserial primary key,
    title varchar not null,
    description text,
    picture text
);