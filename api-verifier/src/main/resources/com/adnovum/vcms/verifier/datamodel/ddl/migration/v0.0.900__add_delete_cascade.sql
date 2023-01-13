ALTER TABLE verified_claim DROP CONSTRAINT fk_claim_state;

ALTER table verified_claim
	ADD CONSTRAINT fk_claim_of_process
		FOREIGN KEY (verification_process_id)
			REFERENCES verification_process
			ON DELETE CASCADE;
