package com.adnovum.vcms.common.security;

import com.adnovum.vcms.common.datamodel.configuration.VcmsCorsConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
@Profile({ "!mockSecurityContext" })
public class ApiSecurityConfig {

	private final VcmsCorsConfiguration vcmsCorsConfiguration;


	@Bean
	@Primary
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors().and()
				.csrf().disable()
				.authorizeRequests().anyRequest().permitAll();

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(vcmsCorsConfiguration.getAllowCredentials());
		configuration.setAllowedOriginPatterns(vcmsCorsConfiguration.getAllowedOriginPatterns());
		configuration.setAllowedHeaders(vcmsCorsConfiguration.getAllowedHeaders());
		configuration.setAllowedMethods(vcmsCorsConfiguration.getAllowedMethods());
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(vcmsCorsConfiguration.getMappingPattern(), configuration);
		return source;
	}
}
