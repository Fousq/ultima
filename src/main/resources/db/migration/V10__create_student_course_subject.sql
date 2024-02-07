CREATE TABLE IF NOT EXISTS student_course_subject(
    course_subject_id bigint not null,
    student_id bigint not null,
    FOREIGN KEY (course_subject_id) REFERENCES course_subject(id),
    FOREIGN KEY (student_id) REFERENCES student(id)
);

ALTER TABLE course_subject
DROP COLUMN visited;