package com.adnovum.vcms.issuer.datamodel.repository;

import java.util.Optional;
import java.util.UUID;

import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "holders", path = "holders")
public interface HolderRepository extends PagingAndSortingRepository<Holder, UUID> {

	Optional<Holder> findByUserId(String userID);
}
