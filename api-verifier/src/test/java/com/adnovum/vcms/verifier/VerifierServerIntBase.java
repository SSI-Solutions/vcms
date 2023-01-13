package com.adnovum.vcms.verifier;

import java.util.List;

import com.adnovum.vcms.common.service.AriesFacadeProperties;
import com.adnovum.vcms.common.test.SharedPostgresqlContainer;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.entity.VerifiedClaim;
import com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState;
import com.adnovum.vcms.verifier.repository.VerificationProcessRepository;
import com.adnovum.vcms.verifier.repository.VerifiedClaimRepository;
import com.adnovum.vcms.verifier.service.VerificationProcessService;
import com.adnovum.vcms.verifier.service.VerifiedClaimService;
import com.adnovum.vcms.verifier.service.VerifierConfiguration;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = SharedPostgresqlContainer.DockerPostgreDataSourceInitializer.class)
@Testcontainers
public abstract class VerifierServerIntBase {
	protected final static String claimKey = "createVerifiedClaimRecord";


	protected final static String invitationUrl =
			"http://localhost:8044?c_i=eyJAdHlwZSI6ICJkaWQ6c292OkJ6Q2JzTlloTXJqSGlxWkRUVUF" +
					"TSGc7c3BlYy9jb25uZWN0aW9ucy8xLjAvaW52aXRhdGlvbiIsICJAaWQiOiAiNzY2YzExYzgtMzEwYS00NjBlLWIyM2EtYzdjYTIyYjkwNzIyIiwgIm"
					+
					"ltYWdlVXJsIjogImh0dHBzOi8vd3d3LmRpdmVzc2kuY29tL3R5cG8zY29uZi9leHQvc3NpX2NvcnBvcmF0ZV9jb3JlL1Jlc291cmNlcy9QdWJsaWMvS"
					+
					"W1hZ2VzL3NzaS1sb2dvLnBuZyIsICJyZWNpcGllbnRLZXlzIjogWyJ1MzVmRE1Od2RSVFI4M3NxakJ3ZU5IY25wY2ZwS3paN210dWhSTWJTckE5Il0s"
					+
					"ICJsYWJlbCI6ICJTU0ktT0lEQy1CcmlkZ2UiLCAic2VydmljZUVuZHBvaW50IjogImh0dHA6Ly9mZDI3LTIxNy0xNjItMS02Lm5ncm9rLmlvIn0=";

	protected Faker faker = new Faker();

	@Autowired
	protected VerifierConfiguration verifierConfiguration;

	@Autowired
	protected VerificationProcessRepository verificationProcessRepository;

	@Autowired
	protected VerifiedClaimRepository verifiedClaimRepository;

	@Autowired
	protected VerificationProcessService verificationProcessService;

	@Autowired
	protected VerifiedClaimService verifiedClaimService;

	@Autowired
	protected AriesFacadeProperties ariesFacadeProperties;

	@BeforeEach
	void cleanupRepos() {
		verifiedClaimRepository.deleteAll();
		verificationProcessRepository.deleteAll();
	}

	protected VerificationProcess createVerificationProcess(String connectionId, String exchangeId) {
		VerificationProcess verificationProcess = new VerificationProcess();
		verificationProcess.setStatus(VerificationProcessState.VERIFICATION_STATE_CREATED);
		verificationProcess.setConnectionId(connectionId);
		verificationProcess.setPresentationExchangeId(exchangeId);
		return verificationProcessRepository.save(verificationProcess);
	}

	protected Iterable<VerifiedClaim> createVerifiedClaimRecordsWithState(VerificationProcess verificationProcess) {

		VerifiedClaim verifiedClaim = new VerifiedClaim();
		verifiedClaim.setClaimKey(claimKey);
		verifiedClaim.setClaimValue(claimKey);
		verifiedClaim.setVerificationProcess(verificationProcess);

		VerifiedClaim verifiedClaim2 = new VerifiedClaim();
		verifiedClaim2.setClaimKey(claimKey + "_2");
		verifiedClaim2.setClaimValue(claimKey + "_2");
		verifiedClaim2.setVerificationProcess(verificationProcess);

		VerifiedClaim verifiedClaim3 = new VerifiedClaim();
		verifiedClaim3.setClaimKey(claimKey + "_3");
		verifiedClaim3.setClaimValue(claimKey + "_3");
		verifiedClaim3.setVerificationProcess(verificationProcess);

		return verifiedClaimRepository.saveAll(List.of(verifiedClaim, verifiedClaim2, verifiedClaim3));
	}

	protected Iterable<VerifiedClaim> createVerifiedClaimRecords(String connectionId, String exchangeId) {
		VerificationProcess verificationProcess = createVerificationProcess(connectionId, exchangeId);
		return createVerifiedClaimRecordsWithState(verificationProcess);
	}
}

