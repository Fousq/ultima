CREATE TABLE IF NOT EXISTS app_user(
    id bigserial primary key,
    username varchar not null unique,
    password varchar not null,
    grant_type varchar not null
);

CREATE TABLE IF NOT EXISTS student(
    id bigserial primary key,
    name varchar not null,
    surname varchar not null,
    email varchar not null,
    phone varchar not null,
    user_id bigint not null,
    foreign key (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS teacher(
    id bigserial primary key,
    name varchar not null unique,
    surname varchar not null,
    email varchar not null,
    phone varchar not null,
    user_id bigint not null,
    foreign key (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS head_teacher(
    id bigserial primary key,
    name varchar not null unique,
    surname varchar not null,
    email varchar not null,
    phone varchar not null,
    user_id bigint not null,
    foreign key (user_id) REFERENCES app_user(id)
);
