package com.adnovum.vcms.verifier.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import javax.transaction.Transactional;

import com.adnovum.vcms.verifier.VerifierServerIntBase;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import org.junit.jupiter.api.Test;

class VerificationProcessRepositoryTest extends VerifierServerIntBase {

	@Test
	void shouldSaveAndFindEntitiesById() {
		String connectionId = UUID.randomUUID().toString();
		String presentationExchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = createVerificationProcess(connectionId, presentationExchangeId);

		assertThat(verificationProcess.getId()).isNotNull();
		verificationProcessRepository.findByPresentationExchangeId(presentationExchangeId).orElseThrow();
	}

	@Test
	void deleteByProcessWithoutClaims() {
		VerificationProcess verificationProcess1 = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());

		VerificationProcess verificationProcess2 = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());

		assertThat(verificationProcessRepository.findById(verificationProcess1.getId()).get()).isNotNull();
		assertThat(verificationProcessRepository.findById(verificationProcess2.getId()).get()).isNotNull();

		verificationProcessRepository.deleteById(verificationProcess1.getId());
		assertThat(verificationProcessRepository.findById(verificationProcess1.getId())).isEmpty();
		assertThat(verificationProcessRepository.findById(verificationProcess2.getId()).get()).isNotNull();

		verificationProcessRepository.deleteById(verificationProcess2.getId());
		assertThat(verificationProcessRepository.findById(verificationProcess1.getId())).isEmpty();
		assertThat(verificationProcessRepository.findById(verificationProcess2.getId())).isEmpty();
	}

	@Test
	@Transactional
	void deletingProcessRemovesClaimsWithCascade() {
		VerificationProcess verificationProcess1 = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		createVerifiedClaimRecordsWithState(verificationProcess1);


		VerificationProcess verificationProcess2 = createVerificationProcess(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		createVerifiedClaimRecordsWithState(verificationProcess2);

		assertThat(verificationProcessRepository.findById(verificationProcess1.getId()).get()).isNotNull();
		assertThat(verificationProcessRepository.findById(verificationProcess2.getId()).get()).isNotNull();
		assertThat(verifiedClaimRepository.findAllByVerificationProcess(verificationProcess1).stream().count()).isEqualTo(3);
		assertThat(verifiedClaimRepository.findAllByVerificationProcess(verificationProcess2).stream().count()).isEqualTo(3);


		verificationProcessRepository.deleteById(verificationProcess1.getId());
		assertThat(verificationProcessRepository.findById(verificationProcess1.getId())).isEmpty();
		assertThat(verificationProcessRepository.findById(verificationProcess2.getId()).get()).isNotNull();
		verificationProcessRepository.flush();
		assertThat(verifiedClaimRepository.findAllByVerificationProcess(verificationProcess1).stream().count()).isZero();
		assertThat(verifiedClaimRepository.findAllByVerificationProcess(verificationProcess2).stream().count()).isEqualTo(3);

		verificationProcessRepository.deleteById(verificationProcess2.getId());
		assertThat(verificationProcessRepository.findById(verificationProcess1.getId())).isEmpty();
		assertThat(verificationProcessRepository.findById(verificationProcess2.getId())).isEmpty();
		verificationProcessRepository.flush();
		assertThat(verifiedClaimRepository.findAllByVerificationProcess(verificationProcess1).stream().count()).isZero();
		assertThat(verifiedClaimRepository.findAllByVerificationProcess(verificationProcess2).stream().count()).isZero();
	}

}
