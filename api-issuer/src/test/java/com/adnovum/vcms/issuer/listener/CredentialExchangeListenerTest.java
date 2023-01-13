package com.adnovum.vcms.issuer.listener;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.adnovum.vcms.common.datamodel.event.CredentialExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.CredentialExchangeState;
import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CredentialExchangeListenerTest extends IssuerServerIntTestBase {

	@Autowired
	CredentialExchangeListener credentialExchangeListener;

	@Test
	void receiveCredentialExchangeEvent() {
		String test = "receiveCredentialExchangeEvent";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		createIssuingProcess(connectionId, credentialExchangeId, test);

		CredentialExchangeEvent credentialExchangeEvent = new CredentialExchangeEvent();
		credentialExchangeEvent.setConnectionId(connectionId);
		credentialExchangeEvent.setCredentialExchangeId(credentialExchangeId);
		credentialExchangeEvent.setCredentialExchangeState(CredentialExchangeState.OFFER_SENT);

		credentialExchangeListener.receiveCredentialExchangeEvent(credentialExchangeEvent);
		IssuingProcess issuingProcess = issuingProcessService.getOptionalByCredentialExchangeId(credentialExchangeId).orElseThrow();
		assertThat(issuingProcess.getProcessState()).isEqualTo(ProcessState.CREDENTIAL_OFFER_SENT);
	}

	@Test
	void fromPresentationExchangeState() {

		ProcessState processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.PROPOSAL_SENT).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.PROCESS_CREATED);

		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.PROPOSAL_RECEIVED).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.PROCESS_CREATED);

		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.OFFER_SENT).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_OFFER_SENT);
		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.OFFER_RECEIVED).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_OFFER_SENT);
		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.REQUEST_SENT).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_OFFER_SENT);

		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.REQUEST_RECEIVED).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_REQUEST_RECEIVED);

		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.CREDENTIAL_ISSUED).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_ISSUED);

		processState =
				CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.CREDENTIAL_ACKED).orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_ISSUED);

		processState = CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.CREDENTIAL_RECEIVED)
				.orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_ISSUED);

		processState = CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.CREDENTIAL_REVOKED)
				.orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.CREDENTIAL_REVOKED);

		processState = CredentialExchangeListener.fromCredentialExchangeState(CredentialExchangeState.STATE_ABANDONED)
				.orElseThrow();
		assertThat(processState).isEqualTo(ProcessState.STATE_ABANDONED);


	}
}
