create table if not exists supervisor(
    id bigserial primary key,
    name varchar not null,
    surname varchar not null,
    email varchar not null,
    phone varchar not null,
    user_id bigint not null,
    bitrix_user_id bigint unique,
    foreign key (user_id) REFERENCES app_user(id)
);