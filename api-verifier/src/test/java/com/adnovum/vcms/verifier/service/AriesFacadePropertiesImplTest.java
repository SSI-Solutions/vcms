package com.adnovum.vcms.verifier.service;

import com.adnovum.vcms.verifier.VerifierServerIntBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AriesFacadePropertiesImplTest extends VerifierServerIntBase {

	@Test
	void getBasePath() {
		assertThat(ariesFacadeProperties.getBasePath()).isNotNull();
	}
}
