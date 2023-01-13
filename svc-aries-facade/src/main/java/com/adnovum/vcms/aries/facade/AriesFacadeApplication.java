package com.adnovum.vcms.aries.facade;

import com.adnovum.vcms.common.exception.ErrorHandler;
import com.adnovum.vcms.common.security.ServiceSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import({ ErrorHandler.class, ServiceSecurityConfig.class })
@SpringBootApplication
public class AriesFacadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AriesFacadeApplication.class, args);
	}
}
