package com.adnovum.vcms.verifier.listener;

import com.adnovum.vcms.common.datamodel.event.PresentationExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeState;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState;
import com.adnovum.vcms.verifier.service.VerificationProcessService;
import com.adnovum.vcms.verifier.service.VerifiedClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.PRESENTATION_VERIFIED;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.STATE_ABANDONED;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.VERIFICATION_PRESENTATION_RECEIVED;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.VERIFICATION_REQUEST_SENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresentProofListener {

	private final VerificationProcessService verificationProcessService;

	private final VerifiedClaimService verifiedClaimService;

	private final ListenerProperties listenerProperties;

	@RabbitListener(queues = "#{listenerProperties.getQueues().get(\"proof\")}")
	public void receivePresentProofEvent(PresentationExchangeEvent presentationExchangeEvent) {
		log.info("Received present proof event {} in state {} from queue {}", presentationExchangeEvent.getPresentationExchangeId(),
				presentationExchangeEvent.getPresentationExchangeState(), listenerProperties.getQueues().get("proof"));

		Optional<VerificationProcessState> processStateOptional =
				fromPresentationExchangeState(presentationExchangeEvent.getPresentationExchangeState());
		Optional<VerificationProcess> verificationProcessOptional = verificationProcessService
				.getVerificationProcessByPresentationExchangeId(presentationExchangeEvent.getPresentationExchangeId());
		// At this point we cannot yet check revocation, only if state is verified

		if (processStateOptional.isPresent() && verificationProcessOptional.isPresent()) {
			VerificationProcessState newProcessState = processStateOptional.get();
			Boolean isPresentationVerified = presentationExchangeEvent.getVerified();
			log.info("Received presentation state= {} ", newProcessState);
			log.info("Presentation verified?= {} ", isPresentationVerified);

			if (VerificationProcessState.PRESENTATION_VERIFIED.equals(newProcessState)) {
				newProcessState = Boolean.TRUE.equals(presentationExchangeEvent.getVerified()) ? newProcessState :
						VerificationProcessState.CREDENTIAL_REVOKED;
			}

			log.info("Updating verification state to {}", newProcessState);
			VerificationProcess verificationProcess = verificationProcessService
					.updateVerificationProcessState(verificationProcessOptional.get(), newProcessState);

			if (VerificationProcessState.PRESENTATION_VERIFIED.equals(verificationProcess.getStatus())) {
				for (Map.Entry<String, String> entry : presentationExchangeEvent.getRevealedAttributes().entrySet()) {
					verifiedClaimService.createAndStoreVerifiedCredential(verificationProcess, entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private Optional<VerificationProcessState> fromPresentationExchangeState(PresentationExchangeState presentationExchangeState) {
		switch (presentationExchangeState) {
			case REQUEST_SENT:
				return Optional.of(VERIFICATION_REQUEST_SENT);
			case REQUEST_RECEIVED:
				return Optional.of(VERIFICATION_PRESENTATION_RECEIVED);
			case VERIFIED:
				return Optional.of(PRESENTATION_VERIFIED);
			case STATE_ABANDONED:
				return Optional.of(STATE_ABANDONED);
			default:
				return Optional.empty();
		}
	}
}
