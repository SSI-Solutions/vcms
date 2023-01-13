package com.adnovum.vcms.connector.service;

import com.adnovum.vcms.connector.ConnectorIntTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AriesFacadePropertiesImplTest extends ConnectorIntTestBase {

	@Test
	void getBasePath() {
		assertThat(ariesFacadeProperties.getBasePath()).isNotNull();
	}
}
