package com.adnovum.vcms.verifier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30).select()
				.apis(RequestHandlerSelectors.basePackage("com.adnovum.vcms.verifier.controller")).paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder().title("VCMS Verifier API")
						.description("API to request verification " + "and user data.").version("1.2.2").build());
	}

}


