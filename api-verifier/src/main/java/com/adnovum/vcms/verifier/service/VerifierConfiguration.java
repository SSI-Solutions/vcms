package com.adnovum.vcms.verifier.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties( prefix = "ssi.configuration" )
@Getter
@Setter
public class VerifierConfiguration {

	private List<String> creDefIds = new ArrayList<>();
}
