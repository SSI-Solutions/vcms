
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

create table issuing_process (
   id varchar(255) not null,
    ctl_cre_ts timestamp,
    ctl_cre_uid varchar(255),
    ctl_mod_ts timestamp,
    ctl_mod_uid varchar(255),
    connection_id uuid not null,
    credential_exchange_id varchar(255) not null,
    process_state varchar(255) not null,
    revocation_state varchar(255),
    holder_id varchar(255),
    primary key (id)
);

