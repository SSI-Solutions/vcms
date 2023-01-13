package com.adnovum.vcms.webhook.service;

import com.adnovum.vcms.common.datamodel.event.ConnectionEvent;
import com.adnovum.vcms.common.datamodel.event.ConnectionState;
import com.adnovum.vcms.common.datamodel.event.CredentialExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.CredentialExchangeState;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeState;
import com.adnovum.vcms.genapi.webhook.server.dto.ConnRecord;
import com.adnovum.vcms.genapi.webhook.server.dto.V10CredentialExchange;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchange;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchange.VerifiedEnum;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchangePresentationRequestedProofRevealedAttrs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class WebhookService {

	private EventService eventService;

	public void connectionEventFromAcaPy(ConnRecord connRecord) {
		log.trace("connection message produced");
		log.debug("produced connection event: {}", connRecord);

		ConnectionState connectionState = ConnectionState.fromValue(connRecord.getState());
		ConnectionEvent connectionEvent = new ConnectionEvent();
		connectionEvent.setConnectionId(UUID.fromString(connRecord.getConnectionId()));
		connectionEvent.setConnectionState(connectionState);

		eventService.sendConnectionEvent(connectionEvent);
		log.trace("connection message sent");
	}

	public void presentProofEventFromAcaPy(V10PresentationExchange presentationExchange) {
		log.trace("present proof message produced");
		log.debug("produced present proof event: {}", presentationExchange);

		PresentationExchangeEvent presentationExchangeEvent = new PresentationExchangeEvent();
		presentationExchangeEvent.setPresentationExchangeId(presentationExchange.getPresentationExchangeId());
		presentationExchangeEvent.setConnectionId(UUID.fromString(Objects.requireNonNull(presentationExchange.getConnectionId())));
		presentationExchangeEvent.setVerified(VerifiedEnum.TRUE.equals(presentationExchange.getVerified()));
		presentationExchangeEvent.setPresentationExchangeState(
				PresentationExchangeState.fromValue(presentationExchange.getState())
		);

		if (presentationExchange.getPresentation() != null) {
			Map<String, V10PresentationExchangePresentationRequestedProofRevealedAttrs> revealedAttrsMap = presentationExchange
					.getPresentation().getRequestedProof().getRevealedAttrs();
			Map<String, String> revealedAttributes = revealedAttrsMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().getRaw()));
			presentationExchangeEvent.setRevealedAttributes(revealedAttributes);
			addReveledAttributesToMap(presentationExchange, presentationExchangeEvent.getRevealedAttributes());
		}

		eventService.sendPresentProofEvent(presentationExchangeEvent);
		log.trace("present proof message sent");
	}

	public void issueCredentialsEventFromAcaPy(V10CredentialExchange v10CredentialExchange) {
		log.trace("issue credential message produced");
		log.debug("produced issue credential event: {}", v10CredentialExchange);

		CredentialExchangeEvent credentialExchangeEvent = new CredentialExchangeEvent();
		credentialExchangeEvent.setConnectionId(UUID.fromString(v10CredentialExchange.getConnectionId()));
		credentialExchangeEvent.setCredentialExchangeId(v10CredentialExchange.getCredentialExchangeId());
		credentialExchangeEvent.setCredentialExchangeState(CredentialExchangeState.fromValue(v10CredentialExchange.getState()));

		eventService.sendIssueCredentialEvent(credentialExchangeEvent);
		log.trace("issue credential message sent");
	}

	private void addReveledAttributesToMap(V10PresentationExchange presentationExchange,
			Map<String, String> mapWithAttributes) {

		// Removed CredDefId, as the implementation was wrong due to the fact that there could be multiple CredDefIds and found
		// no way to efficiently map attributes and credDefId

		Map<String, V10PresentationExchangePresentationRequestedProofRevealedAttrs> rawValues =
				presentationExchange.getPresentation().getRequestedProof().getRevealedAttrs();
		for (Map.Entry<String, V10PresentationExchangePresentationRequestedProofRevealedAttrs> entry : rawValues.entrySet()) {
			log.info("Found attribute '{}' with raw value '{}'", entry.getKey(), entry.getValue().getRaw());
			mapWithAttributes.put(entry.getKey(), entry.getValue().getRaw());
		}
	}
}
