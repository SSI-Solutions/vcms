package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AriesFacadePropertiesImplTest extends IssuerServerIntTestBase {

	@Test
	void getBasePath() {
		assertThat(ariesFacadeProperties.getBasePath()).isNotNull();
	}
}
