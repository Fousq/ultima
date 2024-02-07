create table if not exists currency(
    id bigserial primary key,
    code varchar not null
);

create table if not exists payment_rate(
    id bigserial primary key,
    type varchar not null,
    amount double precision not null default 0.0,
    currency_id bigint not null,
    teacher_id bigint not null,
    foreign key (currency_id) references currency(id),
    foreign key (teacher_id) references teacher(id)
);