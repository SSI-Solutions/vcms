package com.adnovum.vcms.connector.listener;

import com.adnovum.vcms.common.datamodel.event.ConnectionEvent;
import com.adnovum.vcms.common.datamodel.event.ConnectionState;
import com.adnovum.vcms.connector.ConnectorIntTestBase;
import com.adnovum.vcms.connector.datamodel.Connection;
import com.adnovum.vcms.connector.datamodel.ConnectionProcessState;
import com.adnovum.vcms.genapi.connector.server.dto.Invitation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionListenerTest extends ConnectorIntTestBase {

	@Autowired
	ConnectionListener connectionListener;

	@Test
	void receiveConnectionEvent() {
		String test = "receiveConnectionEvent";
		UUID connectionId = UUID.randomUUID();

		assertThat(connectionListenerProperties.getQueues().get("connection")).contains("connectorConnectionQueue");

		Invitation invitation = new Invitation();
		invitation.setConnectionId(connectionId);
		invitation.setInvitationUrl(test + "_invitationUrl");
		connectionService.create(invitation);

		// state change
		ConnectionEvent connectionEvent = new ConnectionEvent();
		connectionEvent.setConnectionId(connectionId);
		connectionEvent.setConnectionState(ConnectionState.REQUEST_SENT);
		connectionListener.receiveConnectionEvent(connectionEvent);

		Connection connection = connectionService.getByConnectionId(connectionId);
		assertThat(connection.getConnectionState()).isEqualTo(ConnectionProcessState.CONNECTION_REQUEST_SENT);
	}

	@Test
	void mapToConnectionProcessState() {

		ConnectionProcessState connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.INVITATION).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_STATE_CREATED);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.INIT).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_INITIATING);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.START).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_INITIATING);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.REQUEST_SENT).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_REQUEST_SENT);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.RESPONSE_RECEIVED).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_RESPONSE_RECEIVED);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.ACTIVE).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_ESTABLISHED);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.COMPLETED).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_ESTABLISHED);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.ERROR).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_CLOSED);

		connectionProcessState = ConnectionListener.mapToConnectionProcessState(ConnectionState.ABANDONED).orElseThrow();
		assertThat(connectionProcessState).isEqualTo(ConnectionProcessState.CONNECTION_CLOSED);
	}
}
