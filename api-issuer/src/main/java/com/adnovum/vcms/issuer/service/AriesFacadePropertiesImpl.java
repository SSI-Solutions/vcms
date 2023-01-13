package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.service.AriesFacadeProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@ConfigurationProperties("aries.facade")
@Component
@Primary
@Getter
@Setter
public class AriesFacadePropertiesImpl implements AriesFacadeProperties {

	private String basePath;
}
