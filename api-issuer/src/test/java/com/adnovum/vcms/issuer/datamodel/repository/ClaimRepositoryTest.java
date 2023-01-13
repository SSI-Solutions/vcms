package com.adnovum.vcms.issuer.datamodel.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.Claim;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionSystemException;

public class ClaimRepositoryTest extends IssuerServerIntTestBase {

	@RepeatedTest(5)
	void claimValueCanBeVeryLong() {
		IssuingProcess process = createIssuingProcess(UUID.randomUUID(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString());

		Claim claim = new Claim();
		claim.setClaimKey(faker.lorem().fixedString(30));
		claim.setClaimValue(faker.lorem().characters(500, true, true, true));
		claim.setIssuingProcess(process);

		process.setClaims(List.of(claim));

		claimRepository.save(claim);
		assertThat(claimRepository.count()).isEqualTo(1);
	}

	@Test
	void claimValueCanBeEmpty() {
		IssuingProcess process = createIssuingProcess(UUID.randomUUID(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		Claim claim = new Claim();
		claim.setClaimKey(faker.lorem().fixedString(30));
		claim.setClaimValue("");
		claim.setIssuingProcess(process);
		claimRepository.save(claim);

		assertThat(claimRepository.count()).isEqualTo(1);
	}

	@Test
	void claimNullValueNotPersisted() {
		IssuingProcess process = createIssuingProcess(UUID.randomUUID(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		Claim claim = new Claim();
		claim.setClaimKey("validKey");
		claim.setClaimValue(null);
		claim.setIssuingProcess(process);
		assertThatThrownBy(() -> {
			claimRepository.save(claim);
		}).isInstanceOf(TransactionSystemException.class);

		assertThat(claimRepository.count()).isEqualTo(0);
	}

	@Test
	void claimKeyCantBeNull() {
		IssuingProcess process = createIssuingProcess(UUID.randomUUID(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		Claim claim = new Claim();
		claim.setClaimKey(null);
		claim.setClaimValue("validValue");
		claim.setIssuingProcess(process);
		assertThatThrownBy(() -> {
			claimRepository.save(claim);
		}).isInstanceOf(TransactionSystemException.class);

		assertThat(claimRepository.count()).isEqualTo(0);
	}
}
