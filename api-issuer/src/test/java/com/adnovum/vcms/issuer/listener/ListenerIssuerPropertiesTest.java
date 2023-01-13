package com.adnovum.vcms.issuer.listener;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ListenerIssuerPropertiesTest extends IssuerServerIntTestBase {

	@Autowired
	ListenerProperties listenerProperties;

	@Test
	void checkProperties() {
		assertThat(listenerProperties.getQueues()).containsEntry("credential", "issuerCredentialQueue");
	}
}
