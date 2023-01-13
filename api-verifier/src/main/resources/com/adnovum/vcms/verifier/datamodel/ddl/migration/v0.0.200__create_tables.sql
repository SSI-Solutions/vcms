
create table verification_state (
									id varchar(255) not null,
									ctl_cre_ts timestamp,
									ctl_cre_uid varchar(255),
									ctl_mod_ts timestamp,
									ctl_mod_uid varchar(255),
									connection_id varchar(255) not null,
									status varchar(255) not null,
									primary key (id)
);

create table verified_claim (
								id varchar(255) not null,
								ctl_cre_ts timestamp,
								ctl_cre_uid varchar(255),
								ctl_mod_ts timestamp,
								ctl_mod_uid varchar(255),
								claim varchar(255),
								value varchar(255),
								verification_state varchar(255) not null,
								primary key (id)
);

