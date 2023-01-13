package com.adnovum.vcms.issuer.datamodel.repository;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class IssuingProcessRepositoryTest extends IssuerServerIntTestBase {

	@Test
	void shouldSaveAndFindProcessEntities() {
		String test = "shouldSaveAndFindProcessEntities";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		createIssuingProcess(connectionId, credentialExchangeId, test);
		IssuingProcess issuingProcess = issuingProcessRepository.findByCredentialExchangeId(credentialExchangeId).orElseThrow();

		assertThat(issuingProcess.getId()).isNotNull();
		assertThat(issuingProcess.getCtlCreTs()).isNotNull();
		assertThat(issuingProcess.getCtlCreUid()).isNotNull();
		assertThat(issuingProcess.getCtlModTs()).isNotNull();
		assertThat(issuingProcess.getCtlModUid()).isNotNull();
		assertThat(issuingProcess.getConnectionId()).isEqualTo(connectionId);
		assertThat(issuingProcess.getCredentialExchangeId()).isEqualTo(credentialExchangeId);
		assertThat(issuingProcess.getProcessState()).isEqualTo(ProcessState.PROCESS_CREATED);
		assertThat(issuingProcess.getHolder().getUserId()).isEqualTo(test);

		assertThat(issuingProcessRepository
				.countIssuingProcessByHolderAndRevocationState(issuingProcess.getHolder(), RevocationState.ISSUED))
				.isZero();

		issuingProcessService.updateRevocationState(issuingProcess.getId(), RevocationState.ISSUED);
		assertThat(issuingProcessRepository
				.countIssuingProcessByHolderAndRevocationState(issuingProcess.getHolder(), RevocationState.ISSUED))
				.isEqualTo(1);
	}
}
