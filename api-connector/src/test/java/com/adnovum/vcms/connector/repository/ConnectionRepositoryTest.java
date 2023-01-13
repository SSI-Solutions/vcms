package com.adnovum.vcms.connector.repository;

import com.adnovum.vcms.connector.ConnectorIntTestBase;
import com.adnovum.vcms.connector.datamodel.Connection;
import com.adnovum.vcms.connector.datamodel.ConnectionProcessState;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionRepositoryTest extends ConnectorIntTestBase {

	@Test
	void findByConnectionId() {
		Connection connection = new Connection();
		connection.setConnectionId(UUID.randomUUID());
		connection.setConnectionState(ConnectionProcessState.CONNECTION_STATE_CREATED);

		connectionRepository.save(connection);
		assertThat(connection.getId()).isNotNull();

		connectionRepository.findByConnectionId(connection.getConnectionId()).orElseThrow();
	}
}
