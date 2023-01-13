alter table authentication_state
	add constraint UK_5duunq1smanfubnyp4kcsoopx unique (connection_id);

alter table Claim
	add constraint UK65c5jw2vhykywb5uydd9brx2w unique (key, credential_id);

alter table Credential
	add constraint UK_guo9bmiehjh82kw3esf94ktbk unique (wallet_credential_exchange_id);

alter table Holder
	add constraint UK_96hk0w8swj7c97sn0fa1bg7l5 unique (userId);

alter table Claim
	add constraint fk_claim_in_cred
		foreign key (credential_id)
			references Credential;

alter table Claim
	add constraint fk_cred_holder
		foreign key (holder_id)
			references Holder;

alter table Connection
	add constraint FKpcrx750igcxb8qxdky8n194e2
		foreign key (holder_id)
			references Holder;

alter table Credential
	add constraint fk_credential_connection
		foreign key (connection_id)
			references Connection;
