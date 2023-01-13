package com.adnovum.vcms.connector.controller;

import com.adnovum.vcms.connector.datamodel.Connection;
import com.adnovum.vcms.connector.service.AriesFacadeService;
import com.adnovum.vcms.connector.service.ConnectionService;
import com.adnovum.vcms.genapi.connector.server.controller.ConnectionApi;
import com.adnovum.vcms.genapi.connector.server.dto.ConnStatus;
import com.adnovum.vcms.genapi.connector.server.dto.Invitation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ConnectionController implements ConnectionApi {

	private final AriesFacadeService ariesFacadeService;

	private final ConnectionService connectionService;

	private final ModelMapper modelMapper = new ModelMapper();

	@Override
	public ResponseEntity<Invitation> connectionInvitation() {
		Invitation controllerConnectionInvitation = modelMapper.map(ariesFacadeService.getInvitation(), Invitation.class);
		connectionService.create(controllerConnectionInvitation);
		return ResponseEntity.ok(controllerConnectionInvitation);
	}

	@Override
	public ResponseEntity<ConnStatus> connectionState(UUID connectionId) {
		Connection connection = connectionService.getByConnectionId(connectionId);
		return ResponseEntity.ok(connection.getConnectionState().getConnStatus());
	}
}
