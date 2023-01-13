package com.adnovum.vcms.common.datamodel.configuration;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "vcms.cors")
@Component
@Getter @Setter
public class VcmsCorsConfiguration {

	Boolean allowCredentials;

	List<String> allowedOriginPatterns;

	List<String> allowedHeaders;

	List<String> allowedMethods;

	String mappingPattern;

}
