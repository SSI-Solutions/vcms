package com.adnovum.vcms.connector.config;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

@Component
public class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter {

	@Value("${server.swagger.url}")
	private String url;

	@Value("${server.swagger.description}")
	private String description;

	@Override
	public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
		OpenAPI swagger = context.getSpecification();
		Server server = new Server();
		server.setUrl(url);
		server.setDescription(description);
		swagger.setServers(Arrays.asList(server));
		return swagger;
	}

	@Override
	public boolean supports(DocumentationType docType) {
		return docType.equals(DocumentationType.OAS_30);
	}
}
