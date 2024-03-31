ALTER TABLE teacher
ADD COLUMN payable_for_canceled_lesson BOOLEAN DEFAULT false NOT NULL;