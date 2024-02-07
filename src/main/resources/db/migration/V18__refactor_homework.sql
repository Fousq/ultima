alter table homework
add column IF NOT EXISTS files text[];

alter table homework
drop constraint IF EXISTS homework_student_id_fkey;

alter table homework
drop column student_id;

alter table homework
rename column name to title;

alter table homework
drop column completed;

create table if not exists student_homework(
    homework_id bigint not null,
    student_id bigint not null,
    completed boolean default false,
    foreign key (homework_id) references homework(id),
    foreign key (student_id) references student(id)
);