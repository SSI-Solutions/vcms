package com.adnovum.vcms.aries.facade.controller;

import com.adnovum.vcms.aries.facade.service.AriesService;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConnectionControllerTest {

	private final ModelMapper modelMapper = new ModelMapper();

	@Test
	void connectionInvitation() {
		String test = "connectionInvitation";

		AriesConnectionInvitation connectionInvitation = new AriesConnectionInvitation();
		connectionInvitation.setConnectionId(UUID.randomUUID());
		connectionInvitation.setInvitationUrl(test);

		AriesService ariesServiceMock = mock(AriesService.class);
		when(ariesServiceMock.getConnectionInvitation()).thenReturn(connectionInvitation);
		ConnectionController connectionController = new ConnectionController(ariesServiceMock);

		assertThat(modelMapper.map(connectionController.connectionInvitation().getBody(), AriesConnectionInvitation.class))
				.isEqualTo(connectionInvitation);
	}

	@Test
	void connectionStateExist() {
		UUID randomId = UUID.randomUUID();
		AriesService ariesServiceMock = mock(AriesService.class);
		when(ariesServiceMock.getConnectionById(String.valueOf(randomId))).thenReturn(AriesConnectionState.CREATED);

		ConnectionController connectionController = new ConnectionController(ariesServiceMock);

		assertThat(connectionController.connectionState(randomId).getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(modelMapper.map(connectionController.connectionState(randomId).getBody(), AriesConnectionState.class))
				.isEqualTo(AriesConnectionState.CREATED);
	}
}
