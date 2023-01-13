
create table authentication_state (
									  id varchar(255) not null,
									  ctl_cre_ts timestamp,
									  ctl_cre_uid varchar(255),
									  ctl_mod_ts timestamp,
									  ctl_mod_uid varchar(255),
									  connection_id varchar(255) not null,
									  status varchar(255) not null,
									  primary key (id)
);

create table Claim (
					   id varchar(255) not null,
					   ctl_cre_ts timestamp,
					   ctl_cre_uid varchar(255),
					   ctl_mod_ts timestamp,
					   ctl_mod_uid varchar(255),
					   key varchar(255),
					   value varchar(255),
					   credential_id varchar(255) not null,
					   holder_id varchar(255) not null,
					   primary key (id)
);

create table Connection (
							id varchar(255) not null,
							ctl_cre_ts timestamp,
							ctl_cre_uid varchar(255),
							ctl_mod_ts timestamp,
							ctl_mod_uid varchar(255),
							holder_id varchar(255),
							primary key (id)
);

create table Credential (
							id varchar(255) not null,
							ctl_cre_ts timestamp,
							ctl_cre_uid varchar(255),
							ctl_mod_ts timestamp,
							ctl_mod_uid varchar(255),
							wallet_credential_exchange_id varchar(255) not null,
							wallet_credential_state varchar(255) not null,
							wallet_revocation_state varchar(255) not null,
							connection_id varchar(255) not null,
							primary key (id)
);

create table Holder (
						id varchar(255) not null,
						ctl_cre_ts timestamp,
						ctl_cre_uid varchar(255),
						ctl_mod_ts timestamp,
						ctl_mod_uid varchar(255),
						userId varchar(255),
						primary key (id)
);

