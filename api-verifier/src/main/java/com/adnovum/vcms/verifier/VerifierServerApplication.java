package com.adnovum.vcms.verifier;

import com.adnovum.vcms.common.configuration.JpaAuditingConfiguration;
import com.adnovum.vcms.common.datamodel.configuration.VcmsCorsConfiguration;
import com.adnovum.vcms.common.exception.ErrorHandler;
import com.adnovum.vcms.common.security.ApiSecurityConfig;
import com.adnovum.vcms.common.service.AriesFacadeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ VcmsCorsConfiguration.class, ErrorHandler.class, JpaAuditingConfiguration.class, ApiSecurityConfig.class,
		AriesFacadeClient.class })
public class VerifierServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerifierServerApplication.class, args);
	}
}
