package com.adnovum.vcms.verifier.repository;

import java.util.List;
import java.util.UUID;

import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.entity.VerifiedClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "verifiedClaims", path = "verifiedClaims")
public interface VerifiedClaimRepository extends JpaRepository<VerifiedClaim, UUID> {

	List<VerifiedClaim> findAllByVerificationProcess(VerificationProcess verificationProcess);

}
