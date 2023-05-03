package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.service.AriesFacadeProperties;
import com.adnovum.vcms.common.service.VcmsFeatureProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@ConfigurationProperties("vcms.features")
@Component
@Primary
@Getter
@Setter
public class VcmsFeaturesPropertiesImpl implements VcmsFeatureProperties {

	private Boolean storeClaims;

}
