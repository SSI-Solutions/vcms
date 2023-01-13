ALTER TABLE verified_claim RENAME claim to claim_key;
ALTER TABLE verified_claim ALTER COLUMN claim_key SET NOT NULL;

ALTER TABLE verified_claim RENAME value to claim_value;
ALTER TABLE verified_claim ALTER COLUMN claim_value SET NOT NULL;
