package com.adnovum.vcms.webhook.service;

import com.adnovum.vcms.common.datamodel.event.ConnectionEvent;
import com.adnovum.vcms.common.datamodel.event.CredentialExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

	private final RabbitTemplate rabbitTemplate;

	private final DirectExchange connectionExchange;

	private final DirectExchange presentationExchange;

	private final DirectExchange credentialExchange;

	public void sendConnectionEvent(ConnectionEvent connectionEvent){
		rabbitTemplate.convertAndSend(connectionExchange.getName(), connectionExchange.getName(), connectionEvent);
		log.debug("Send connection event to MQ = {} - {}",
				connectionEvent.getConnectionId(),
				connectionEvent.getConnectionState()
		);
	}

	public void sendPresentProofEvent(PresentationExchangeEvent presentationExchangeEvent){
		rabbitTemplate.convertAndSend(presentationExchange.getName(), presentationExchange.getName(), presentationExchangeEvent);
		log.debug("Send presentation exchange event to MQ = connectionId: {} - presentationExchangeId: {}, exchangeState: {}",
				presentationExchangeEvent.getConnectionId(),
				presentationExchangeEvent.getPresentationExchangeId(),
				presentationExchangeEvent.getPresentationExchangeState()
		);
	}

	public void sendIssueCredentialEvent(CredentialExchangeEvent credentialExchangeEvent) {
		rabbitTemplate.convertAndSend(credentialExchange.getName(), credentialExchange.getName(), credentialExchangeEvent);
		log.debug("Send credential exchange event to MQ = connectionId: {} - credentialExchangeId: {}, credentialExchangeState: {}",
				credentialExchangeEvent.getConnectionId(),
				credentialExchangeEvent.getCredentialExchangeId(),
				credentialExchangeEvent.getCredentialExchangeState()
		);
	}
}
