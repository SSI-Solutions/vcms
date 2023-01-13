package com.adnovum.vcms.issuer.listener;

import com.adnovum.vcms.common.datamodel.event.CredentialExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.CredentialExchangeState;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import com.adnovum.vcms.issuer.service.AriesFacadeService;
import com.adnovum.vcms.issuer.service.IssuingProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.CREDENTIAL_ISSUED;
import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.CREDENTIAL_OFFER_SENT;
import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.CREDENTIAL_REQUEST_RECEIVED;
import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.CREDENTIAL_REVOKED;
import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.PROCESS_CREATED;
import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.STATE_ABANDONED;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialExchangeListener {

	private final IssuingProcessService issuingProcessService;

	private final ListenerProperties listenerProperties;

	private final AriesFacadeService ariesFacadeService;

	@RabbitListener(queues = "#{listenerProperties.getQueues().get(\"credential\")}")
	public void receiveCredentialExchangeEvent(CredentialExchangeEvent credentialExchangeEvent) {
		String credentialExchangeId = credentialExchangeEvent.getCredentialExchangeId();
		CredentialExchangeState credentialExchangeState = credentialExchangeEvent.getCredentialExchangeState();

		log.info("Received credential exchange event {} in state {} from queue {}", credentialExchangeId, credentialExchangeState,
				listenerProperties.getQueues().get("credential"));

		Optional<IssuingProcess> issuingProcessOptional = issuingProcessService.getOptionalByCredentialExchangeId(credentialExchangeId);
		Optional<ProcessState> processStateOptional = fromCredentialExchangeState(credentialExchangeState);

		if (processStateOptional.isPresent() && issuingProcessOptional.isPresent()) {
			issuingProcessService.updateProcessState(issuingProcessOptional.get().getId(), processStateOptional.get());

			if (ProcessState.CREDENTIAL_REQUEST_RECEIVED.equals(processStateOptional.get())) {
				ariesFacadeService.issueCredential(UUID.fromString(credentialExchangeId));
				issuingProcessService.updateRevocationState(issuingProcessOptional.get().getId(), RevocationState.ISSUED);
			}

			if (ProcessState.CREDENTIAL_REVOKED.equals(processStateOptional.get())) {
				issuingProcessService.updateRevocationState(issuingProcessOptional.get().getId(), RevocationState.REVOKED);
			}
		}
	}

	protected static Optional<ProcessState> fromCredentialExchangeState(CredentialExchangeState credentialExchangeState) {
		switch (credentialExchangeState) {
			case PROPOSAL_SENT:
				return Optional.of(PROCESS_CREATED);
			case PROPOSAL_RECEIVED:
				return Optional.of(PROCESS_CREATED);
			case OFFER_SENT:
				return Optional.of(CREDENTIAL_OFFER_SENT);
			case OFFER_RECEIVED:
				return Optional.of(CREDENTIAL_OFFER_SENT);
			case REQUEST_SENT:
				return Optional.of(CREDENTIAL_OFFER_SENT);
			case REQUEST_RECEIVED:
				return Optional.of(CREDENTIAL_REQUEST_RECEIVED);
			case CREDENTIAL_RECEIVED:
				return Optional.of(CREDENTIAL_ISSUED);
			case CREDENTIAL_ACKED:
				return Optional.of(CREDENTIAL_ISSUED);
			case CREDENTIAL_ISSUED:
				return Optional.of(CREDENTIAL_ISSUED);
			case CREDENTIAL_REVOKED:
				return Optional.of(CREDENTIAL_REVOKED);
			case STATE_ABANDONED:
				return Optional.of(STATE_ABANDONED);
			default:
				return Optional.empty();
		}
	}
}
