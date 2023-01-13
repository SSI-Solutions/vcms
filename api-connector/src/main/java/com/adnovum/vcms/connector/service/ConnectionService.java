package com.adnovum.vcms.connector.service;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.connector.datamodel.Connection;
import com.adnovum.vcms.connector.datamodel.ConnectionProcessState;
import com.adnovum.vcms.connector.repository.ConnectionRepository;
import com.adnovum.vcms.genapi.connector.server.dto.Invitation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.adnovum.vcms.common.exception.BusinessReason.ERROR_CONNECTION_NOT_EXISTENT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConnectionService {

	private final ConnectionRepository connectionRepository;

	public Connection create(Invitation invitation) {
		Connection connection = new Connection();
		connection.setConnectionId(invitation.getConnectionId());
		connection.setConnectionState(ConnectionProcessState.CONNECTION_STATE_CREATED);

		return connectionRepository.save(connection);
	}

	public Connection updateState(UUID connectionId, ConnectionProcessState connectionProcessState) {
		Connection connection = getByConnectionId(connectionId);

		if (connection != null && !connection.getConnectionState().equals(connectionProcessState)) {
			log.debug("Update state of connection {} from {} to {}", connectionId, connection.getConnectionState(),
					connectionProcessState);
			connection.setConnectionState(connectionProcessState);
			connectionRepository.save(connection);
		}
		return connection;
	}

	public Connection getByConnectionId(UUID connectionId) {
		return getOptionalByConnectionId(connectionId).orElseThrow(() ->
				new BusinessException("Cannot resolve the given {id} = " + connectionId, ERROR_CONNECTION_NOT_EXISTENT));
	}

	public Optional<Connection> getOptionalByConnectionId(UUID connectionId) {
		return connectionRepository.findByConnectionId(connectionId);
	}
}
