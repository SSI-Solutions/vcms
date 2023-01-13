package com.adnovum.vcms.issuer.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(properties = "vcms.cors.allowedMethods=GET,PUT")
class OverrideSecurityConfigTest extends IssuerServerIntTestBase {


	@Test
	void testCorsGetIsAllowed() throws Exception {

		String origin = "http://www." + faker.lorem().word() + ".com";

		mvc.perform(options("/")
				.header("Access-Control-Request-Method", "GET")
				.header("Origin", origin))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", origin))
				.andExpect(header().string("Access-Control-Allow-Methods", "GET,PUT"))
				.andExpect(header().string("Access-Control-Allow-Credentials", "true"))
				.andDo(print())
				.andReturn();
	}

	@Test
	void testCorsPutIsAllowed() throws Exception {

		String origin = "http://www." + faker.lorem().word() + ".com";

		mvc.perform(options("/")
				.header("Access-Control-Request-Method", "PUT")
				.header("Origin", origin))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", origin))
				.andExpect(header().string("Access-Control-Allow-Methods", "GET,PUT"))
				.andExpect(header().string("Access-Control-Allow-Credentials", "true"))
				.andDo(print())
				.andReturn();
	}

	@Test
	void testCorsDeleteDenied() throws Exception {


		String origin = "http://www." + faker.lorem().word() + ".com";

		mvc.perform(options("/")
				.header("Access-Control-Request-Method", "DELETE")
				.header("Origin", origin))
				.andExpect(status().is4xxClientError())
				.andDo(print())
				.andReturn();
	}
}
