alter table claim 
   add constraint UKle048ulml7q8fcil4lclqsg0i unique (claim_key, issuing_process_id);

alter table holder 
   add constraint UK_k7ij326faqt5pd00nax81blx unique (userId);

alter table issuing_process 
   add constraint UK_gaobr1wpechsc3s0f8dtaecj unique (credential_exchange_id);

alter table claim 
   add constraint fk_claim_in_process 
   foreign key (issuing_process_id) 
   references issuing_process;

alter table issuing_process 
   add constraint FKd70b68i9q88ce59ad1gomt1wi 
   foreign key (holder_id) 
   references holder;
