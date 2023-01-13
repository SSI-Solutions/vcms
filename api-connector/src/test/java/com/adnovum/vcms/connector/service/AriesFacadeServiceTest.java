package com.adnovum.vcms.connector.service;

import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionInvitation;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AriesFacadeServiceTest {

	@Test
	void getInvitation() {
		UUID randomUuid = UUID.randomUUID();
		String test = "getInvitation";

		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);
		AriesConnectionInvitation controllerConnectionInvitation = new AriesConnectionInvitation();
		controllerConnectionInvitation.setConnectionId(randomUuid);
		controllerConnectionInvitation.setInvitationUrl(test);
		when(ariesFacadeClientMock.getInvitation()).thenReturn(controllerConnectionInvitation);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);
		controllerConnectionInvitation = ariesFacadeService.getInvitation();

		assertThat(controllerConnectionInvitation.getConnectionId()).isEqualTo(randomUuid);
	}
}
