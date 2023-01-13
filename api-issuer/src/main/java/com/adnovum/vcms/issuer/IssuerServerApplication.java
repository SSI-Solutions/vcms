package com.adnovum.vcms.issuer;

import com.adnovum.vcms.common.configuration.JpaAuditingConfiguration;
import com.adnovum.vcms.common.datamodel.configuration.VcmsCorsConfiguration;
import com.adnovum.vcms.common.exception.ErrorHandler;
import com.adnovum.vcms.common.security.ApiSecurityConfig;
import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.ApiClient;
import com.adnovum.vcms.genapi.aries.facade.client.controller.RevocationApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Import({ VcmsCorsConfiguration.class, ErrorHandler.class, JpaAuditingConfiguration.class, ApiSecurityConfig.class,
		AriesFacadeClient.class})
public class IssuerServerApplication {

	@Value("${aries.facade.basePath}")
	private String ariesFacadeBasePath = "";

	public static void main(String[] args) {
		SpringApplication.run(IssuerServerApplication.class, args);
	}

	@Bean
	public RevocationApi apiClient() {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(ariesFacadeBasePath);
		return new RevocationApi(apiClient);
	}
}
