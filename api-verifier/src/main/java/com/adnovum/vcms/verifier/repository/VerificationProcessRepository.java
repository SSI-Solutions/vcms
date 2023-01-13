package com.adnovum.vcms.verifier.repository;

import java.util.Optional;
import java.util.UUID;

import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "verificationStates", path = "verificationStates")
public interface VerificationProcessRepository extends JpaRepository<VerificationProcess, UUID> {

	Optional<VerificationProcess> findByPresentationExchangeId(String presentationExchangeId);

	boolean existsVerificationProcessById(UUID id);

	void deleteById(UUID id);
}
