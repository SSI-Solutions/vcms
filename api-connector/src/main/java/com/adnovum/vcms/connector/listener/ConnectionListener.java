package com.adnovum.vcms.connector.listener;

import com.adnovum.vcms.common.datamodel.event.ConnectionEvent;
import com.adnovum.vcms.common.datamodel.event.ConnectionState;
import com.adnovum.vcms.connector.datamodel.Connection;
import com.adnovum.vcms.connector.datamodel.ConnectionProcessState;
import com.adnovum.vcms.connector.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.adnovum.vcms.connector.datamodel.ConnectionProcessState.CONNECTION_CLOSED;
import static com.adnovum.vcms.connector.datamodel.ConnectionProcessState.CONNECTION_ESTABLISHED;
import static com.adnovum.vcms.connector.datamodel.ConnectionProcessState.CONNECTION_INITIATING;
import static com.adnovum.vcms.connector.datamodel.ConnectionProcessState.CONNECTION_REQUEST_SENT;
import static com.adnovum.vcms.connector.datamodel.ConnectionProcessState.CONNECTION_RESPONSE_RECEIVED;
import static com.adnovum.vcms.connector.datamodel.ConnectionProcessState.CONNECTION_STATE_CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionListener {

	private final ConnectionService connectionService;

	private final ConnectionListenerProperties connectionListenerProperties;

	@RabbitListener(queues = "#{connectionListenerProperties.getQueues().get(\"connection\")}")
	public void receiveConnectionEvent(ConnectionEvent connectionEvent) {
		log.info("Received connection event {} in state {} from queue {}", connectionEvent.getConnectionId(),
				connectionEvent.getConnectionState(), connectionListenerProperties.getQueues().get("connection"));

		Optional<Connection> connectionOptional = connectionService.getOptionalByConnectionId(connectionEvent.getConnectionId());
		if (connectionOptional.isPresent()) {
			Optional<ConnectionProcessState> connectionProcessStateOptional = mapToConnectionProcessState(connectionEvent.getConnectionState());
			connectionProcessStateOptional.ifPresent(connectionProcessState
					-> connectionService.updateState(connectionEvent.getConnectionId(), connectionProcessState));
		}
	}

	protected static Optional<ConnectionProcessState> mapToConnectionProcessState(ConnectionState connectionState) {
		switch (connectionState) {
			case INIT:
				return Optional.of(CONNECTION_INITIATING);
			case START:
				return Optional.of(CONNECTION_INITIATING);
			case INVITATION:
				return Optional.of(CONNECTION_STATE_CREATED);
			case REQUEST_SENT:
				return Optional.of(CONNECTION_REQUEST_SENT);
			case RESPONSE_RECEIVED:
				return Optional.of(CONNECTION_RESPONSE_RECEIVED);
			case COMPLETED:
				return Optional.of(CONNECTION_ESTABLISHED);
			case ACTIVE:
				return Optional.of(CONNECTION_ESTABLISHED);
			case ERROR:
				return Optional.of(CONNECTION_CLOSED);
			case ABANDONED:
				return Optional.of(CONNECTION_CLOSED);
			default:
				return Optional.empty();
		}
	}
}
