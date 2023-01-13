CREATE SCHEMA IF NOT EXISTS utils ;
CREATE EXTENSION IF NOT EXISTS pgcrypto SCHEMA utils;

ALTER TABLE authentication_state rename to issuing_state;

ALTER TABLE issuing_state
	ADD COLUMN credential_exchange_id varchar(255) not null DEFAULT utils.gen_random_uuid();

ALTER TABLE issuing_state
	DROP CONSTRAINT UK_5duunq1smanfubnyp4kcsoopx;

alter table issuing_state
	add constraint UK_j28j1xiohytgobs3fuvm0juxx unique (credential_exchange_id);
