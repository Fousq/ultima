ALTER TABLE student
ADD CONSTRAINT student_phone_unique UNIQUE (phone);

ALTER TABLE student
ALTER COLUMN phone SET NOT NULL;