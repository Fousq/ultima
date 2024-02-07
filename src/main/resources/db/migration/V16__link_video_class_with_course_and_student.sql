ALTER TABLE video_class
ADD COLUMN course_subject_id bigint;

ALTER TABLE video_class
ADD CONSTRAINT video_class_course_subject_id_fkey
FOREIGN KEY (course_subject_id)
REFERENCES course_subject(id);

ALTER TABLE video_class
ADD COLUMN teacher_id bigint;

ALTER TABLE video_class
ADD CONSTRAINT video_class_teacher_id_fkey
FOREIGN KEY (teacher_id)
REFERENCES teacher(id);

CREATE TABLE IF NOT EXISTS student_video_class(
    student_id bigint not null,
    video_class_id bigint not null,
    visited boolean default false,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (video_class_id) REFERENCES video_class(id)
);
