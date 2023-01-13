package com.adnovum.vcms.connector.datamodel;

import com.adnovum.vcms.common.configuration.JpaConfigurationBase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Import({ DataSourceAutoConfiguration.class })
public class JpaConfiguration extends JpaConfigurationBase {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder builder, DataSource dataSource) {
		return entityManagerFactoryBase(builder, dataSource)
				.persistenceUnit("CONNECTOR-UNIT")
				.build();
	}
}
