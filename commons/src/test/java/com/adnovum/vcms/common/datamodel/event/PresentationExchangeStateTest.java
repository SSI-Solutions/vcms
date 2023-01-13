package com.adnovum.vcms.common.datamodel.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class PresentationExchangeStateTest {

	@Test
	void checkMappings() {
		assertThat(PresentationExchangeState.fromValue("proposal_sent")).isEqualTo(PresentationExchangeState.PROPOSAL_SENT);
		assertThat(PresentationExchangeState.fromValue("proposal_received")).isEqualTo(
				PresentationExchangeState.PROPOSAL_RECEIVED);
		assertThat(PresentationExchangeState.fromValue("request_sent")).isEqualTo(PresentationExchangeState.REQUEST_SENT);
		assertThat(PresentationExchangeState.fromValue("request_received")).isEqualTo(PresentationExchangeState.REQUEST_RECEIVED);
		assertThat(PresentationExchangeState.fromValue("presentation_sent")).isEqualTo(
				PresentationExchangeState.PRESENTATION_SENT);
		assertThat(PresentationExchangeState.fromValue("presentation_received")).isEqualTo(
				PresentationExchangeState.PRESENTATION_RECEIVED);
		assertThat(PresentationExchangeState.fromValue("presentation_acked")).isEqualTo(
				PresentationExchangeState.PRESENTATION_ACKED);
		assertThat(PresentationExchangeState.fromValue("verified")).isEqualTo(PresentationExchangeState.VERIFIED);
		assertThat(PresentationExchangeState.fromValue("abandoned")).isEqualTo(PresentationExchangeState.STATE_ABANDONED);



	}

}
