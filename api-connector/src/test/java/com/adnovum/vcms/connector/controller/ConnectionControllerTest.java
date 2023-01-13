package com.adnovum.vcms.connector.controller;

import com.adnovum.vcms.connector.ConnectorIntTestBase;
import com.adnovum.vcms.connector.service.AriesFacadeService;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.connector.server.dto.ConnStatus;
import com.adnovum.vcms.genapi.connector.server.dto.Invitation;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConnectionControllerTest extends ConnectorIntTestBase {

	private final ModelMapper modelMapper = new ModelMapper();

	@Test
	void connectionInvitation() {
		String test = "connectionInvitation";

		AriesConnectionInvitation connectionInvitation = new AriesConnectionInvitation();
		connectionInvitation.setConnectionId(UUID.randomUUID());
		connectionInvitation.setInvitationUrl(test);
		AriesFacadeService ariesFacadeServiceMock = mock(AriesFacadeService.class);
		when(ariesFacadeServiceMock.getInvitation()).thenReturn(connectionInvitation);

		ConnectionController connectionController = new ConnectionController(ariesFacadeServiceMock, connectionService);
		assertThat(modelMapper.map(connectionController.connectionInvitation().getBody(), AriesConnectionInvitation.class))
				.isEqualTo(connectionInvitation);
	}

	@Test
	void connectionState() {
		String test = "connectionState";
		UUID randomId = UUID.randomUUID();

		AriesFacadeService ariesFacadeServiceMock = mock(AriesFacadeService.class);
		ConnectionController connectionController = new ConnectionController(ariesFacadeServiceMock, connectionService);

		Invitation invitation = new Invitation();
		invitation.setConnectionId(randomId);
		invitation.setInvitationUrl(test + "_invitationUrl");
		connectionService.create(invitation);

		assertThat(connectionController.connectionState(randomId).getBody()).isEqualTo(ConnStatus.CREATED);
	}
}
