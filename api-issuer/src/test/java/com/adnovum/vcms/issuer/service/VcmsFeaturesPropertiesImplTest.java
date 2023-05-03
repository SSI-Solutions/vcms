package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VcmsFeaturesPropertiesImplTest extends IssuerServerIntTestBase {

	@Test
	void getStoreClaims() {
		assertThat(vcmsFeatureProperties.getStoreClaims()).isNotNull();
	}
}
