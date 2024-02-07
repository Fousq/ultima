ALTER TABLE course_subject
ADD COLUMN duration int not null default 0;

ALTER TABLE course_subject
ADD COLUMN start_at timestamp not null default now();