create table if not exists time_package(
    id bigserial primary key,
    amount_in_minutes int not null default 0,
    type varchar not null,
    student_id bigint not null,
    foreign key (student_id) references student(id)
);