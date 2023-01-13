package com.adnovum.vcms.webhook.service;

import com.adnovum.vcms.common.datamodel.event.ConnectionEvent;
import com.adnovum.vcms.common.datamodel.event.ConnectionState;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeState;
import com.adnovum.vcms.genapi.webhook.server.dto.ConnRecord;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchange;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchange.VerifiedEnum;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchangePresentation;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchangePresentationRequestedProof;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchangePresentationRequestedProofRevealedAttrs;
import com.adnovum.vcms.webhook.WebhookIntBase;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WebhookServiceTest extends WebhookIntBase {

	@Test
	void convertConnectionEventFromAcapy() {
		UUID randomId = UUID.randomUUID();

		// response -> RESPONDED

		EventService eventServiceMock = mock(EventService.class);
		WebhookService webhookService = new WebhookService(eventServiceMock);

		ConnRecord connRecord = new ConnRecord();
		connRecord.setConnectionId(randomId.toString());
		connRecord.setState("response");
		webhookService.connectionEventFromAcaPy(connRecord);

		ArgumentCaptor<ConnectionEvent> argument = ArgumentCaptor.forClass(ConnectionEvent.class);
		verify(eventServiceMock).sendConnectionEvent(argument.capture());
		ConnectionEvent connectionEvent = argument.getValue();

		assertThat(connectionEvent.getConnectionId()).isEqualTo(randomId);
		assertThat(connectionEvent.getConnectionState()).isEqualTo(ConnectionState.RESPONSE_RECEIVED);

		// invitation -> CREATED

		eventServiceMock = mock(EventService.class);
		webhookService = new WebhookService(eventServiceMock);

		connRecord.setState("invitation");
		webhookService.connectionEventFromAcaPy(connRecord);

		verify(eventServiceMock).sendConnectionEvent(argument.capture());
		connectionEvent = argument.getValue();
		assertThat(connectionEvent.getConnectionId()).isEqualTo(randomId);
		assertThat(connectionEvent.getConnectionState()).isEqualTo(ConnectionState.INVITATION);

		// request -> REQUESTED

		eventServiceMock = mock(EventService.class);
		webhookService = new WebhookService(eventServiceMock);

		connRecord.setState("request");
		webhookService.connectionEventFromAcaPy(connRecord);

		verify(eventServiceMock).sendConnectionEvent(argument.capture());
		connectionEvent = argument.getValue();
		assertThat(connectionEvent.getConnectionId()).isEqualTo(randomId);
		assertThat(connectionEvent.getConnectionState()).isEqualTo(ConnectionState.REQUEST_SENT);

		// completed -> ESTABLISHED

		eventServiceMock = mock(EventService.class);
		webhookService = new WebhookService(eventServiceMock);

		connRecord.setState("completed");
		webhookService.connectionEventFromAcaPy(connRecord);

		verify(eventServiceMock).sendConnectionEvent(argument.capture());
		connectionEvent = argument.getValue();
		assertThat(connectionEvent.getConnectionId()).isEqualTo(randomId);
		assertThat(connectionEvent.getConnectionState()).isEqualTo(ConnectionState.COMPLETED);
	}

	@Test
	void failForUnknownState() {
		UUID randomId = UUID.randomUUID();

		EventService eventServiceMock = mock(EventService.class);
		WebhookService webhookService = new WebhookService(eventServiceMock);

		ConnRecord connRecord = new ConnRecord();
		connRecord.setConnectionId(randomId.toString());
		connRecord.setState("failForUnknownState");
		assertThatThrownBy(() -> webhookService.connectionEventFromAcaPy(connRecord)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void convertPresentationExchangeFromAcapy() {
		UUID connectionId = UUID.randomUUID();
		String presentationExchangeId = UUID.randomUUID().toString();

		V10PresentationExchangePresentationRequestedProofRevealedAttrs nameAttribute =
				new V10PresentationExchangePresentationRequestedProofRevealedAttrs();
		nameAttribute.setRaw("John Smith");
		V10PresentationExchangePresentationRequestedProofRevealedAttrs birthdayAttribute =
				new V10PresentationExchangePresentationRequestedProofRevealedAttrs();
		birthdayAttribute.setRaw("01.01.1970");

		V10PresentationExchangePresentationRequestedProof requestedProof =
				new V10PresentationExchangePresentationRequestedProof();
		requestedProof.putRevealedAttrsItem("name", nameAttribute);
		requestedProof.putRevealedAttrsItem("birthday", birthdayAttribute);

		V10PresentationExchangePresentation presentationExchangePresentation = new V10PresentationExchangePresentation();
		presentationExchangePresentation.setRequestedProof(requestedProof);

		V10PresentationExchange presentationExchange = new V10PresentationExchange();
		presentationExchange.setConnectionId(connectionId.toString());
		presentationExchange.setPresentationExchangeId(presentationExchangeId);
		presentationExchange.setState("verified");
		presentationExchange.setVerified(VerifiedEnum.TRUE);
		presentationExchange.setPresentation(presentationExchangePresentation);


		EventService eventService = mock(EventService.class);
		WebhookService webhookService = new WebhookService(eventService);
		webhookService.presentProofEventFromAcaPy(presentationExchange);

		ArgumentCaptor<PresentationExchangeEvent> argument = ArgumentCaptor.forClass(PresentationExchangeEvent.class);
		verify(eventService).sendPresentProofEvent(argument.capture());
		PresentationExchangeEvent presentationExchangeEvent = argument.getValue();

		assertThat(presentationExchangeEvent.getConnectionId()).isEqualTo(connectionId);
		assertThat(presentationExchangeEvent.getVerified()).isTrue();
		assertThat(presentationExchangeEvent.getPresentationExchangeId()).isEqualTo(presentationExchangeId);
		assertThat(presentationExchangeEvent.getPresentationExchangeState()).isEqualTo(PresentationExchangeState.VERIFIED);
		assertThat(presentationExchangeEvent.getRevealedAttributes()).containsEntry("name", "John Smith");
		assertThat(presentationExchangeEvent.getRevealedAttributes()).containsEntry("birthday", "01.01.1970");


		presentationExchange.setVerified(VerifiedEnum.FALSE);
		eventService = mock(EventService.class);
		webhookService = new WebhookService(eventService);
		webhookService.presentProofEventFromAcaPy(presentationExchange);
		argument = ArgumentCaptor.forClass(PresentationExchangeEvent.class);
		verify(eventService).sendPresentProofEvent(argument.capture());
		presentationExchangeEvent = argument.getValue();
		assertThat(presentationExchangeEvent.getVerified()).isFalse();
	}
}
