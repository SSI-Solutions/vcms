package com.adnovum.vcms.verifier.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import com.adnovum.vcms.verifier.VerifierServerIntBase;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.entity.VerifiedClaim;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VerifiedClaimServiceTest extends VerifierServerIntBase {

	@Autowired
	VerifiedClaimService verifiedClaimService;

	@Test
	void createVerifiedClaim() {
		String test = "createVerifiedClaim";
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = createVerificationProcess(connectionId, exchangeId);
		VerifiedClaim verifiedClaim = verifiedClaimService.createAndStoreVerifiedCredential(verificationProcess, test, test);
		List<VerifiedClaim> verifiedClaims = verifiedClaimRepository.findAllByVerificationProcess(verificationProcess);
		assertThat(verifiedClaims).hasSize(1);
		assertThat(verifiedClaims.get(0).getId()).isEqualTo(verifiedClaim.getId());
	}

	@Test
	void createVerifiedClaimWithEmptyValue() {
		String key = "createVerifiedClaim";
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = createVerificationProcess(connectionId, exchangeId);
		VerifiedClaim verifiedClaim = verifiedClaimService.createAndStoreVerifiedCredential(verificationProcess, key, null);
		List<VerifiedClaim> verifiedClaims = verifiedClaimRepository.findAllByVerificationProcess(verificationProcess);
		assertThat(verifiedClaims).hasSize(1);
		assertThat(verifiedClaims.get(0).getId()).isEqualTo(verifiedClaim.getId());
		assertThat(verifiedClaims.get(0).getClaimValue()).isEmpty();
	}

	@Test
	void doNotCreateClaimWithoutKey() {
		String value = "createVerifiedClaim";
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = createVerificationProcess(connectionId, exchangeId);
		verifiedClaimService.createAndStoreVerifiedCredential(verificationProcess, null, value);
		List<VerifiedClaim> verifiedClaims = verifiedClaimRepository.findAllByVerificationProcess(verificationProcess);
		assertThat(verifiedClaims).isEmpty();
	}

	@Test
	void getVerifiedClaimsByVerificationProcess() {
		String test = "getVerifiedClaimsByVerificationProcess";
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = createVerificationProcess(connectionId, exchangeId);
		VerifiedClaim verifiedClaim = verifiedClaimService.createAndStoreVerifiedCredential(verificationProcess, test, test);
		VerifiedClaim verifiedClaim1 =
				verifiedClaimService.createAndStoreVerifiedCredential(verificationProcess, test + "_2", test + "_2");
		List<UUID> uuids = List.of(verifiedClaim.getId(), verifiedClaim1.getId());

		List<VerifiedClaim> verifiedClaims = verifiedClaimService.getVerifiedClaimsByVerificationProcess(verificationProcess);
		assertThat(verifiedClaims).hasSize(2);
		assertThat(verifiedClaims.get(0)).isNotEqualTo(verifiedClaims.get(1));
		verifiedClaims.forEach(x -> assertThat(uuids).contains(x.getId()));
	}

}
