alter table verification_process 
   add constraint UK_fqy5vjlb9441f5okyn9fbinhj unique (presentation_exchange_id);

alter table verified_claim 
   add constraint fk_claim_of_process 
   foreign key (verification_process_id) 
   references verification_process 
   on delete cascade;
