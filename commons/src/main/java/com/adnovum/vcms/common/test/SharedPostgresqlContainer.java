package com.adnovum.vcms.common.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class SharedPostgresqlContainer {

	private SharedPostgresqlContainer() {}

	private static final String IMAGE_VERSION = "postgres:14-alpine";

	/**
	 * Set <code>testcontainers.reuse.enable=true</code> in <code>~/.testcontainers.properties</code> to enable container reuse.
	 * Then stop and remove the container manually when no longer needed.
	 */
	@Container
	private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(IMAGE_VERSION)
			.withReuse(true)
			.withDatabaseName("postgres")
			.withUsername("sa")
			.withPassword("sa");

	/**
	 * Can't use @{@link DynamicPropertySource} outside of a test class, so rather this than using inheritance, which is a bad
	 * practice.
	 * <p>
	 * Adapt the properties to your configured data sources (in our case: {@link DemoDataSourceConfiguration}).
	 */
	public static class DockerPostgreDataSourceInitializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(final @NotNull ConfigurableApplicationContext applicationContext) {
			if (!postgresqlContainer.isRunning()) {
				postgresqlContainer.start();
			}

			TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
					"spring.datasource.url=" + postgresqlContainer.getJdbcUrl(),
					"spring.datasource.driver-class-name=" + postgresqlContainer.getDriverClassName(),
					"spring.datasource.username=" + postgresqlContainer.getUsername(),
					"spring.datasource.password=" + postgresqlContainer.getPassword(),
					"spring.flyway.url=" + postgresqlContainer.getJdbcUrl(),
					"spring.flyway.user=" + postgresqlContainer.getUsername(),
					"spring.flyway.password=" + postgresqlContainer.getPassword(),
					"spring.flyway.driver=" + postgresqlContainer.getDriverClassName()
			);
		}
	}

}
