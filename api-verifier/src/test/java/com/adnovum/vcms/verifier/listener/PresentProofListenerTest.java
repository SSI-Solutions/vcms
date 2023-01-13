package com.adnovum.vcms.verifier.listener;

import com.adnovum.vcms.common.datamodel.event.PresentationExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.PresentationExchangeState;
import com.adnovum.vcms.verifier.VerifierServerIntBase;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.UUID;

import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.CREDENTIAL_REVOKED;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.PRESENTATION_VERIFIED;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.STATE_ABANDONED;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.VERIFICATION_REQUEST_SENT;
import static com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState.VERIFICATION_STATE_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

class PresentProofListenerTest extends VerifierServerIntBase {

	@Autowired
	ListenerProperties listenerProperties;

	@Test
	void receivePresentProofEvent() {
		String test = "receivePresentProofEvent";
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		createVerificationProcess(connectionId, exchangeId);

		PresentProofListener presentProofListener = new PresentProofListener(verificationProcessService, verifiedClaimService, listenerProperties);

		// state change
		PresentationExchangeEvent presentationExchangeEvent = new PresentationExchangeEvent();
		presentationExchangeEvent.setPresentationExchangeId(test);
		presentationExchangeEvent.setPresentationExchangeState(PresentationExchangeState.REQUEST_SENT);
		presentationExchangeEvent.setConnectionId(UUID.fromString(connectionId));
		presentationExchangeEvent.setPresentationExchangeId(exchangeId);
		presentationExchangeEvent.setRevealedAttributes(Collections.emptyMap());

		presentProofListener.receivePresentProofEvent(presentationExchangeEvent);
		VerificationProcess verificationProcess = verificationProcessRepository.findByPresentationExchangeId(exchangeId).orElseThrow();
		assertThat(verificationProcess.getStatus()).isEqualTo(VERIFICATION_REQUEST_SENT);

		// no change
		presentationExchangeEvent.setPresentationExchangeState(PresentationExchangeState.PRESENTATION_ACKED);
		presentProofListener.receivePresentProofEvent(presentationExchangeEvent);
		verificationProcess = verificationProcessRepository.findByPresentationExchangeId(exchangeId).orElseThrow();
		assertThat(verificationProcess.getStatus()).isEqualTo(VERIFICATION_REQUEST_SENT);

		// ready for verification
		verificationProcess.setStatus(VERIFICATION_STATE_CREATED);
		verificationProcessRepository.save(verificationProcess);
		presentProofListener.receivePresentProofEvent(presentationExchangeEvent);
		verificationProcess = verificationProcessRepository.findByPresentationExchangeId(exchangeId).orElseThrow();
		assertThat(verificationProcess.getStatus()).isEqualTo(VERIFICATION_STATE_CREATED);

		// Verified
		presentationExchangeEvent.setPresentationExchangeState(PresentationExchangeState.VERIFIED);
		presentationExchangeEvent.setVerified(Boolean.TRUE);
		presentProofListener.receivePresentProofEvent(presentationExchangeEvent);
		verificationProcess = verificationProcessRepository.findByPresentationExchangeId(exchangeId)
				.orElseThrow();
		assertThat(verificationProcess.getStatus()).isEqualTo(PRESENTATION_VERIFIED);

		// Revoked
		presentationExchangeEvent.setPresentationExchangeState(PresentationExchangeState.VERIFIED);
		presentationExchangeEvent.setVerified(Boolean.FALSE);
		presentProofListener.receivePresentProofEvent(presentationExchangeEvent);
		verificationProcess = verificationProcessRepository.findByPresentationExchangeId(exchangeId)
				.orElseThrow();
		assertThat(verificationProcess.getStatus()).isEqualTo(CREDENTIAL_REVOKED);

		// Abandoned
		presentationExchangeEvent.setPresentationExchangeState(PresentationExchangeState.STATE_ABANDONED);
		presentationExchangeEvent.setVerified(Boolean.FALSE);
		presentProofListener.receivePresentProofEvent(presentationExchangeEvent);
		verificationProcess = verificationProcessRepository.findByPresentationExchangeId(exchangeId)
				.orElseThrow();
		assertThat(verificationProcess.getStatus()).isEqualTo(STATE_ABANDONED);
	}

	@Test
	void MessageQueueProperties() {
		assertThat(listenerProperties.getQueues().get("proof")).contains("verifierPresentationQueue");
	}
}
