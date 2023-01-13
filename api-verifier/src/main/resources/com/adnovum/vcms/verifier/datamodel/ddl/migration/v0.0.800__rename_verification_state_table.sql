ALTER TABLE verification_state
	RENAME TO verification_process;

ALTER TABLE verified_claim RENAME verification_state to verification_process_id;

