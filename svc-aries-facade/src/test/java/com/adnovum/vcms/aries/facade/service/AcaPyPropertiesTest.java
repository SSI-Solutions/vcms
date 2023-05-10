package com.adnovum.vcms.aries.facade.service;

import com.adnovum.vcms.aries.facade.AriesFacadeIntBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AcaPyPropertiesTest extends AriesFacadeIntBase {

	@Test
	void getBaseAcaPyPropertiesAreSet() {
		assertThat(acaPyProperties.getBasePath()).isNotNull();
		assertThat(acaPyProperties.getNonce()).isEqualTo("78789");
		assertThat(acaPyProperties.getComment()).isNotNull();
		assertThat(acaPyProperties.getProofName()).isNotNull();
		assertThat(acaPyProperties.getCredentialOfferTrace()).isTrue();
		assertThat(acaPyProperties.getCredentialOfferAutoRemove()).isFalse();
		assertThat(acaPyProperties.getCredentialOfferAutoIssue()).isFalse();
	}
}
