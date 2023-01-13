alter table verification_state
	add constraint UK_fny85s4f1tw9bc5brbtij34fn unique (connection_id);

alter table verified_claim
	add constraint fk_claim_state
		foreign key (verification_state)
			references verification_state;
