package com.adnovum.vcms.connector;

import com.adnovum.vcms.common.service.AriesFacadeProperties;
import com.adnovum.vcms.common.test.SharedPostgresqlContainer;
import com.adnovum.vcms.connector.listener.ConnectionListenerProperties;
import com.adnovum.vcms.connector.repository.ConnectionRepository;
import com.adnovum.vcms.connector.service.ConnectionService;
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
public abstract class ConnectorIntTestBase {

	@Autowired
	protected ConnectionListenerProperties connectionListenerProperties;

	@Autowired
	protected AriesFacadeProperties ariesFacadeProperties;

	@Autowired
	protected ConnectionRepository connectionRepository;

	@Autowired
	protected ConnectionService connectionService;

	@BeforeEach
	void cleanup() {
		connectionRepository.deleteAll();
	}
}
