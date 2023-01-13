package com.adnovum.vcms.issuer.datamodel.repository;

import com.adnovum.vcms.issuer.datamodel.entity.Claim;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "claims", path = "claims")
public interface ClaimRepository extends PagingAndSortingRepository<Claim, UUID> {

	Set<Claim> findAllByIssuingProcessId(UUID id);
}
