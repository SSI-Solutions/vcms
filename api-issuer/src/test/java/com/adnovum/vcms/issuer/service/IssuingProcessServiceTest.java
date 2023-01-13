package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.CREDENTIAL_ISSUED;
import static com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState.PROCESS_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

class IssuingProcessServiceTest extends IssuerServerIntTestBase {

	@Test
	void all() {
		String test = "IssuingProcessServiceTest";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		Holder holder = createTestHolder(test);

		issuingProcessService.createIssuingProcess(connectionId, credentialExchangeId, holder);
		IssuingProcess issuingProcess = issuingProcessService.getOptionalByCredentialExchangeId(credentialExchangeId).orElseThrow();
		assertThat(issuingProcess.getProcessState()).isEqualTo(PROCESS_CREATED);
		assertThat(issuingProcess.getConnectionId()).isEqualTo(connectionId);
		assertThat(issuingProcess.getCredentialExchangeId()).isEqualTo(credentialExchangeId);

		issuingProcess = issuingProcessService.updateProcessState(issuingProcess.getId(), CREDENTIAL_ISSUED);

		issuingProcess = issuingProcessService.getIssuingProcessById(issuingProcess.getId());
		assertThat(issuingProcess.getProcessState()).isEqualTo(CREDENTIAL_ISSUED);
		assertThat(issuingProcess.getConnectionId()).isEqualTo(connectionId);
	}
}
