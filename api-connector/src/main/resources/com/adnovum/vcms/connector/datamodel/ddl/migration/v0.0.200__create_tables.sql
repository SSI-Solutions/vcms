
create table Connection (
   id varchar(255) not null,
    ctl_cre_ts timestamp,
    ctl_cre_uid varchar(255),
    ctl_mod_ts timestamp,
    ctl_mod_uid varchar(255),
    connection_id varchar(255) not null,
    connection_state varchar(255) not null,
    primary key (id)
);

