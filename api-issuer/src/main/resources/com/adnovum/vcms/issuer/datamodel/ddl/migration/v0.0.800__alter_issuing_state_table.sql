ALTER TABLE issuing_state rename to issuing_process;

ALTER TABLE issuing_process
    RENAME COLUMN status TO process_state;

ALTER TABLE issuing_process
	ADD COLUMN revocation_state varchar(255);

ALTER TABLE issuing_process
	ADD COLUMN holder_id varchar(255);

ALTER TABLE issuing_process
	ADD CONSTRAINT FKd70b68i9q88ce59ad1gomt1wi
		FOREIGN KEY (holder_id)
			REFERENCES holder;
