CREATE SCHEMA IF NOT EXISTS utils ;
CREATE EXTENSION IF NOT EXISTS pgcrypto SCHEMA utils;

ALTER TABLE verification_state
	ADD COLUMN presentation_exchange_id varchar(255) not null DEFAULT utils.gen_random_uuid();

ALTER TABLE verification_state
	DROP CONSTRAINT UK_fny85s4f1tw9bc5brbtij34fn;

ALTER TABLE verification_state
	ADD CONSTRAINT UK_2tg7llwfm203e3kyrljptxcbu unique (presentation_exchange_id);
