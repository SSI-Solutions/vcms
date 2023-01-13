package com.adnovum.vcms.aries.facade.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("acapy")
@Component
@Getter
@Setter
public class AcaPyProperties {

	private String basePath;

	private String comment = "present proof request";

	private String proofName = "present proof request";

	private String nonce = "123456789";
}
