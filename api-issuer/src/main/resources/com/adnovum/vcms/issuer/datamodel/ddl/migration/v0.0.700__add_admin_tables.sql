DROP TABLE Claim;
DROP TABLE Credential;
DROP TABLE Connection;
DROP TABLE Holder;

create table claim (
					   id varchar(255) not null,
					   ctl_cre_ts timestamp,
					   ctl_cre_uid varchar(255),
					   ctl_mod_ts timestamp,
					   ctl_mod_uid varchar(255),
					   claim_key varchar(255) not null,
					   claim_value varchar(255) not null,
					   issuing_process_id varchar(255) not null,
					   primary key (id)
);

create table holder (
						id varchar(255) not null,
						ctl_cre_ts timestamp,
						ctl_cre_uid varchar(255),
						ctl_mod_ts timestamp,
						ctl_mod_uid varchar(255),
						userId varchar(255) not null,
						primary key (id)
);

alter table claim
	add constraint UKle048ulml7q8fcil4lclqsg0i unique (claim_key, issuing_process_id);

alter table holder
	add constraint UK_k7ij326faqt5pd00nax81blx unique (userId);

alter table claim
	add constraint fk_claim_in_process
		foreign key (issuing_process_id)
			references issuing_state;
