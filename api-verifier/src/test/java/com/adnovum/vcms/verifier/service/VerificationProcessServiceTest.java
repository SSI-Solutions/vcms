package com.adnovum.vcms.verifier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.verifier.VerifierServerIntBase;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VerificationProcessServiceTest extends VerifierServerIntBase {

	@Autowired
	VerificationProcessService verificationProcessService;

	// Note the service is also covered through the Listener tests.
	@Test
	void deleteProcessWithoutClaims() {
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = verificationProcessService.createVerificationProcess(connectionId, exchangeId);
		assertThat(verificationProcessService.getVerificationProcessById(verificationProcess.getId())).isNotNull();
		verificationProcessService.deleteByProcess(verificationProcess);

		UUID newProcessId = verificationProcess.getId();

		assertThatThrownBy(() ->
			verificationProcessService.getVerificationProcessById(newProcessId)
		).isInstanceOf(BusinessException.class);
	}

	@Test
	void deleteProcessWithClaims() {
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = verificationProcessService.createVerificationProcess(connectionId, exchangeId);
		createVerifiedClaimRecordsWithState(verificationProcess);
		assertThat(verificationProcessService.getVerificationProcessById(verificationProcess.getId())).isNotNull();
		assertThat(verifiedClaimService.getVerifiedClaimsByVerificationProcess(verificationProcess)).hasSize(3);

		verificationProcessService.deleteByProcess(verificationProcess);

		UUID newProcessId = verificationProcess.getId();

		assertThatThrownBy(() ->
			verificationProcessService.getVerificationProcessById(newProcessId)
		).isInstanceOf(BusinessException.class);
		assertThat(verifiedClaimService.getVerifiedClaimsByVerificationProcess(verificationProcess)).isEmpty();
	}

}
