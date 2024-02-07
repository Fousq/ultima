CREATE TABLE IF NOT EXISTS course(
    id bigserial primary key,
    title varchar not null unique,
    description text,
    duration varchar
);

CREATE TABLE IF NOT EXISTS student_course(
    id bigserial primary key,
    student_id bigint not null,
    course_id bigint not null,
    foreign key (student_id) REFERENCES student(id),
    foreign key (course_id) REFERENCES course(id)
);

CREATE TABLE IF NOT EXISTS teacher_course(
    id bigserial primary key,
    teacher_id bigint not null,
    course_id bigint not null,
    foreign key (teacher_id) REFERENCES teacher(id),
    foreign key (course_id) REFERENCES course(id)
);

CREATE TABLE IF NOT EXISTS course_subject(
    id bigserial primary key,
    title varchar not null unique,
    description text,
    course_id bigint,
    foreign key (course_id) REFERENCES course(id)
);

CREATE TABLE IF NOT EXISTS homework(
    id bigserial primary key,
    name varchar not null unique,
    description text,
    completed boolean default false,
    course_subject_id bigint not null,
    student_id bigint not null,
    foreign key (course_subject_id) REFERENCES course_subject(id),
    foreign key (student_id) REFERENCES student(id)
);