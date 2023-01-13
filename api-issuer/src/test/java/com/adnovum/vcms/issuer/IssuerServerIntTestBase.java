package com.adnovum.vcms.issuer;

import java.util.Map;
import java.util.UUID;
import javax.servlet.Filter;

import com.adnovum.vcms.common.test.SharedPostgresqlContainer;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.repository.ClaimRepository;
import com.adnovum.vcms.issuer.datamodel.repository.HolderRepository;
import com.adnovum.vcms.issuer.datamodel.repository.IssuingProcessRepository;
import com.adnovum.vcms.issuer.service.ClaimService;
import com.adnovum.vcms.issuer.service.HolderService;
import com.adnovum.vcms.issuer.service.IssuingProcessService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = SharedPostgresqlContainer.DockerPostgreDataSourceInitializer.class)
@Testcontainers
public abstract class IssuerServerIntTestBase {

	protected Faker faker = new Faker();

	protected MockMvc mvc;


	@Autowired
	protected HolderRepository holderRepository;

	@Autowired
	protected HolderService holderService;

	@Autowired
	protected ClaimRepository claimRepository;

	@Autowired
	protected ClaimService claimService;

	@Autowired
	protected ModelMapper modelMapper;

	@Autowired
	protected IssuingProcessRepository issuingProcessRepository;

	@Autowired
	protected IssuingProcessService issuingProcessService;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Filter springSecurityFilterChain;

	@BeforeEach
	void cleanup() {
		claimRepository.deleteAll();
		holderRepository.deleteAll();
		issuingProcessRepository.deleteAll();

		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
	}

	protected Holder createTestHolder(String holderId) {
		Holder holder = new Holder();
		holder.setUserId(holderId);
		return holderRepository.save(holder);
	}

	protected IssuingProcess createIssuingProcess(UUID connectionId, String credentialExchangeId, String userId) {
		Holder holder = createHolder(userId);
		return issuingProcessService.createIssuingProcess(connectionId, credentialExchangeId, holder);
	}

	protected Holder createHolder(String userId) {
		Holder holder = new Holder();
		holder.setUserId(userId);
		return holderRepository.save(holder);
	}

	protected void persistTestClaimsFor(IssuingProcess issuingProcess, String test) {
		Map<String, String> claims = Map.of(
				test + "_key1", test + "_value1",
				test + "_key2", test + "_value2",
				test + "_key3", test + "_value3");
		claimService.createClaims(issuingProcess, claims);
	}
}
