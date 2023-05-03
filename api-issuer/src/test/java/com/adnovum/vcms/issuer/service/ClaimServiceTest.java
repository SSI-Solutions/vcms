package com.adnovum.vcms.issuer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.adnovum.vcms.common.service.VcmsFeatureProperties;
import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.Claim;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.repository.ClaimRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ClaimServiceTest extends IssuerServerIntTestBase {

	@Test
	void createClaims() {
		String test = "createClaims";
		Map<String, String> claimNames = Map.of(
				test + "_key1", test + "_value1",
				test + "_key2", test + "_value2",
				test + "_key3", test + "_value3");
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		claimService.persistClaims(issuingProcess, claimNames);

		Set<Claim> claims = claimRepository.findAllByIssuingProcessId(issuingProcess.getId());
		assertThat(claims).hasSize(3);
		claims.forEach(x -> {
			assertThat(x.getIssuingProcess().getId()).isEqualTo(issuingProcess.getId());
			assertThat(x.getClaimValue()).isEqualTo(claimNames.get(x.getClaimKey()));
		});

		IssuingProcess issuingProcess1 = issuingProcessService.getIssuingProcessById(issuingProcess.getId());
		claimService.persistClaims(issuingProcess1, Map.of(test + "_key4", test + "_value4"));
		claims = claimRepository.findAllByIssuingProcessId(issuingProcess1.getId());
		assertThat(claims).hasSize(4);
	}

	@Test
	void createClaimWithEmptyValue() {
		String test = "createClaims";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		claimService.persistClaims(issuingProcess, Map.of(
				"keyWithEmptyValue", "")
		);

		Set<Claim> claims = claimRepository.findAllByIssuingProcessId(issuingProcess.getId());
		assertThat(claims).hasSize(1);
		assertThat(claims.iterator().next().getClaimValue()).isEqualTo("");
	}

	@Test
	void createNullClaimAsEmpty() {
		String test = "createClaims";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		final Map<String, String> newClaims = new HashMap<String, String>();
		newClaims.put("conn_id", null);
		claimService.persistClaims(issuingProcess, newClaims);

		Set<Claim> claims = claimRepository.findAllByIssuingProcessId(issuingProcess.getId());
		assertThat(claims).hasSize(1);
		assertThat(claims.iterator().next().getClaimValue()).isEqualTo("");
	}

	@Test
	void claimWithNullKeyisDropped() {
		String test = "createClaims";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		HashMap<String, String> newClaims = new HashMap<>();
		newClaims.put(null, "validValue");
		claimService.persistClaims(issuingProcess, newClaims);

		Set<Claim> claims = claimRepository.findAllByIssuingProcessId(issuingProcess.getId());
		assertThat(claims).hasSize(0);
	}

	@Test
	void claimPersistenceCanBeTurnedOff() {
		VcmsFeaturesPropertiesImpl turnedOffFeatureFlag = new VcmsFeaturesPropertiesImpl();
		turnedOffFeatureFlag.setStoreClaims(Boolean.FALSE);
		ClaimRepository mockedRepo = mock(ClaimRepository.class);
		ClaimService claimServiceNoPersistence = new ClaimService(mockedRepo,turnedOffFeatureFlag);
		String test = "createClaims";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		HashMap<String, String> newClaims = new HashMap<>();
		newClaims.put("validKey", "validValue");

		claimServiceNoPersistence.persistClaims(issuingProcess,newClaims);
		Mockito.verifyNoMoreInteractions(mockedRepo);

		// Testing the control behaviour of persistence
		claimService.persistClaims(issuingProcess, newClaims);
		Set<Claim> claims = claimRepository.findAllByIssuingProcessId(issuingProcess.getId());
		assertThat(claims).hasSize(1);
	}
}
