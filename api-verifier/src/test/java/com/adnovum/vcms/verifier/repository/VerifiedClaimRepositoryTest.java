package com.adnovum.vcms.verifier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import com.adnovum.vcms.verifier.VerifierServerIntBase;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.entity.VerifiedClaim;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionSystemException;

class VerifiedClaimRepositoryTest extends VerifierServerIntBase {

	@Test
	void shouldSaveAndFindEntitiesById() {
		String connectionId = UUID.randomUUID().toString();
		String presentationExchangeId = UUID.randomUUID().toString();

		VerifiedClaim verifiedClaim = createVerifiedClaimRecords(connectionId, presentationExchangeId).iterator().next();
		assertThat(verifiedClaim.getId()).isNotNull();

		VerificationProcess verificationProcess =
				verificationProcessRepository.findByPresentationExchangeId(verifiedClaim.getVerificationProcess()
						.getPresentationExchangeId()).orElseThrow();
		List<VerifiedClaim> verifiedClaims = verifiedClaimRepository.findAllByVerificationProcess(verificationProcess);
		assertThat(verifiedClaims).hasSize(3);
	}

	@RepeatedTest(5)
	void claimValueCanBeVeryLong() {
		VerificationProcess verificationProcess = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		VerifiedClaim claim = new VerifiedClaim();
		claim.setClaimKey(faker.lorem().fixedString(30));
		claim.setClaimValue(faker.lorem().characters(500, true, true, true));
		claim.setVerificationProcess(verificationProcess);
		verifiedClaimRepository.save(claim);
		AssertionsForClassTypes.assertThat(verifiedClaimRepository.count()).isEqualTo(1);

	}

	@Test
	void claimValueCanBeEmpty() {
		VerificationProcess verificationProcess = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		VerifiedClaim claim = new VerifiedClaim();
		claim.setClaimKey(faker.lorem().fixedString(30));
		claim.setClaimValue("");
		claim.setVerificationProcess(verificationProcess);
		verifiedClaimRepository.save(claim);
		AssertionsForClassTypes.assertThat(verifiedClaimRepository.count()).isEqualTo(1);
	}

	@Test
	void nullValueCannotBeStored() {
		VerificationProcess verificationProcess = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		VerifiedClaim claim = new VerifiedClaim();
		claim.setVerificationProcess(verificationProcess);

		claim.setClaimKey("validKey");
		claim.setClaimValue(null);
		assertThatThrownBy(() -> {
			verifiedClaimRepository.save(claim);
		}).isInstanceOf(TransactionSystemException.class);

		AssertionsForClassTypes.assertThat(verifiedClaimRepository.count()).isEqualTo(0);
	}

	@Test
	void nullKeyCannotBeStored() {
		VerificationProcess verificationProcess = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		VerifiedClaim claim = new VerifiedClaim();
		claim.setVerificationProcess(verificationProcess);

		claim.setClaimKey(null);
		claim.setClaimValue("validValue");

		assertThatThrownBy(() -> {
			verifiedClaimRepository.save(claim);
		}).isInstanceOf(TransactionSystemException.class);

		AssertionsForClassTypes.assertThat(verifiedClaimRepository.count()).isEqualTo(0);
	}
}
