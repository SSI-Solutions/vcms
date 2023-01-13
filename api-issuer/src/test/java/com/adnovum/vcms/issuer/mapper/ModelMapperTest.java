package com.adnovum.vcms.issuer.mapper;

import com.adnovum.vcms.genapi.issuer.server.dto.CredentialResponse;
import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperTest extends IssuerServerIntTestBase {

	@Autowired
	protected ModelMapper modelMapper;

	@Test
	void testCredentialToCredentialResponseMap() {
		String test = "testCredentialToCredentialResponseMap";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		issuingProcess.setRevocationState(RevocationState.ISSUED);

		CredentialResponse credentialResponse = modelMapper.map(issuingProcess, CredentialResponse.class);

		assertThat(credentialResponse.getConnectionId()).isEqualTo(issuingProcess.getConnectionId());
		assertThat(credentialResponse.getCredentialId()).isEqualTo(issuingProcess.getId().toString());
		assertThat(credentialResponse.getProcessState()).isEqualTo(issuingProcess.getProcessState().getValue());
		assertThat(credentialResponse.getRevocationState()).isEqualTo(issuingProcess.getRevocationState().getValue());
	}
}
