package com.adnovum.vcms.webhook.service;

import com.adnovum.vcms.common.datamodel.event.ConnectionEvent;
import com.adnovum.vcms.common.datamodel.event.ConnectionState;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeState;
import com.adnovum.vcms.webhook.WebhookIntBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EventServiceTest extends WebhookIntBase {

	@Autowired
	DirectExchange connectionExchange;

	@Autowired
	DirectExchange presentationExchange;

	@Autowired
	DirectExchange credentialExchange;

	@Test
	@ExtendWith(OutputCaptureExtension.class)
	void sendConnectionEvent(CapturedOutput output) {
		RabbitTemplate rabbitTemplateMock = mock(RabbitTemplate.class);
		EventService eventService = new EventService(rabbitTemplateMock, connectionExchange, presentationExchange, credentialExchange);

		ConnectionEvent connectionEvent = new ConnectionEvent();
		connectionEvent.setConnectionId(UUID.randomUUID());
		connectionEvent.setConnectionState(ConnectionState.INVITATION);
		eventService.sendConnectionEvent(connectionEvent);

		assertThat(output).contains(String.format("Send connection event to MQ = %s - %s", connectionEvent.getConnectionId(),
				connectionEvent.getConnectionState()));
	}

	@Test
	@ExtendWith(OutputCaptureExtension.class)
	void sendPresentProofEvent(CapturedOutput output) {
		String test = "sendPresentProofEvent";
		RabbitTemplate rabbitTemplateMock = mock(RabbitTemplate.class);
		EventService eventService = new EventService(rabbitTemplateMock, connectionExchange, presentationExchange, credentialExchange);

		PresentationExchangeEvent presentationExchangeEvent = new PresentationExchangeEvent();
		presentationExchangeEvent.setConnectionId(UUID.randomUUID());
		presentationExchangeEvent.setPresentationExchangeState(PresentationExchangeState.PROPOSAL_SENT);
		presentationExchangeEvent.setPresentationExchangeId(test);
		presentationExchangeEvent.setRevealedAttributes(Map.of("name", test));
		eventService.sendPresentProofEvent(presentationExchangeEvent);

		assertThat(output).contains(String.format(
				"Send presentation exchange event to MQ = connectionId: %s - presentationExchangeId: %s, exchangeState: %s",
				presentationExchangeEvent.getConnectionId(),
				presentationExchangeEvent.getPresentationExchangeId(),
				presentationExchangeEvent.getPresentationExchangeState()
		));
	}
}
