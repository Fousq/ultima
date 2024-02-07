ALTER TABLE time_package
ADD CONSTRAINT time_package_unique_type_and_student_id
UNIQUE (type, student_id);