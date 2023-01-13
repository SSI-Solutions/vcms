package com.adnovum.vcms.connector;

import com.adnovum.vcms.common.configuration.JpaAuditingConfiguration;
import com.adnovum.vcms.common.datamodel.configuration.VcmsCorsConfiguration;
import com.adnovum.vcms.common.exception.ErrorHandler;
import com.adnovum.vcms.common.security.ApiSecurityConfig;
import com.adnovum.vcms.common.service.AriesFacadeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ VcmsCorsConfiguration.class, ErrorHandler.class, ApiSecurityConfig.class, JpaAuditingConfiguration.class,
		AriesFacadeClient.class })
public class ConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectorApplication.class, args);
	}
}
