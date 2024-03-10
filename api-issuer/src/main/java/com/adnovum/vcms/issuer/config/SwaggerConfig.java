package com.adnovum.vcms.issuer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30).select()
				.apis(RequestHandlerSelectors.basePackage("com.adnovum.vcms.issuer.controller")).paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder().title("VCMS Issuer and Admin API")
						.description("Issue Verifiable " + "Credentails and manage credential holders").version("1.2.1").build());
	}

}


