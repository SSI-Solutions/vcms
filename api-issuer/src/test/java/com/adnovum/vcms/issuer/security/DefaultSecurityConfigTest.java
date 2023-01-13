package com.adnovum.vcms.issuer.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DefaultSecurityConfigTest extends IssuerServerIntTestBase {

	@ParameterizedTest
	@ValueSource(strings = { "GET", "HEAD", "PUT", "POST", "DELETE" })
	void testDefaultCorsSettingAllowsAccess(String header) throws Exception {

		String origin = "http://www." + faker.lorem().word() + ".com";

		mvc.perform(options("/")
				.header("Access-Control-Request-Method", header)
				.header("Origin", origin))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", origin))
				.andExpect(header().string("Access-Control-Allow-Methods", header))
				.andExpect(header().string("Access-Control-Allow-Credentials", "true"))
				.andDo(print())
				.andReturn();
	}
}
