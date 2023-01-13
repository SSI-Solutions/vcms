package com.adnovum.vcms.connector.repository;

import com.adnovum.vcms.connector.datamodel.Connection;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConnectionRepository extends PagingAndSortingRepository<Connection, UUID> {

	Optional<Connection> findByConnectionId(UUID connectionID);
}
