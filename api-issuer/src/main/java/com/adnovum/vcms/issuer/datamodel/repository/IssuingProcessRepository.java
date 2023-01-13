package com.adnovum.vcms.issuer.datamodel.repository;

import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "issuingProcess", path = "issuingProcesses")
public interface IssuingProcessRepository extends PagingAndSortingRepository<IssuingProcess, UUID> {

	Optional<IssuingProcess> findByCredentialExchangeId(String credentialExchangeId);

	int countIssuingProcessByHolderAndRevocationState(Holder holder, RevocationState revocationState);
}
