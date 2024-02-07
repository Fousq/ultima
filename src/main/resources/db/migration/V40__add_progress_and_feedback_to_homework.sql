ALTER TABLE student_homework
ADD COLUMN in_progress boolean default false;

ALTER TABLE student_homework
ADD COLUMN feedback text;