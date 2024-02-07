ALTER TABLE student
ADD COLUMN bitrix_contact_id bigint unique;

ALTER TABLE teacher
ADD COLUMN bitrix_user_id bigint unique;

ALTER TABLE head_teacher
ADD COLUMN bitrix_user_id bigint unique;