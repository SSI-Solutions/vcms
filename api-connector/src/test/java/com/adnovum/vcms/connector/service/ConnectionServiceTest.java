package com.adnovum.vcms.connector.service;

import com.adnovum.vcms.connector.ConnectorIntTestBase;
import com.adnovum.vcms.connector.datamodel.Connection;
import com.adnovum.vcms.connector.datamodel.ConnectionProcessState;
import com.adnovum.vcms.genapi.connector.server.dto.Invitation;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionServiceTest extends ConnectorIntTestBase {

	@Test
	void all() {
		UUID randomUuid = UUID.randomUUID();
		String test = "all";

		Invitation invitation = new Invitation();
		invitation.setConnectionId(randomUuid);
		invitation.setInvitationUrl(test);

		connectionService.create(invitation);
		Connection connection1 = connectionService.getByConnectionId(randomUuid);
		assertThat(connection1.getConnectionState()).isEqualTo(ConnectionProcessState.CONNECTION_STATE_CREATED);
		assertThat(connection1.getConnectionId()).isEqualTo(randomUuid);

		connectionService.updateState(randomUuid, ConnectionProcessState.CONNECTION_ESTABLISHED);
		Connection connection2 = connectionService.getByConnectionId(randomUuid);
		assertThat(connection2.getConnectionState()).isEqualTo(ConnectionProcessState.CONNECTION_ESTABLISHED);
		assertThat(connection2.getConnectionId()).isEqualTo(randomUuid);
	}
}
