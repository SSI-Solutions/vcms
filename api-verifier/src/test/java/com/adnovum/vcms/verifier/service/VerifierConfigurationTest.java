package com.adnovum.vcms.verifier.service;

import com.adnovum.vcms.verifier.VerifierServerIntBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VerifierConfigurationTest extends VerifierServerIntBase {

	@Test
	void injectMap(){
		assertThat(verifierConfiguration.getCreDefIds()).isNotNull();
		assertThat(verifierConfiguration.getCreDefIds()).hasSize(2);
	}
}
