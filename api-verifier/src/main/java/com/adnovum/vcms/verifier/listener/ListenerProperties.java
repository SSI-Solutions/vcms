package com.adnovum.vcms.verifier.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(value = "vcms.rabbitmq")
@Component
@Getter @Setter
public class ListenerProperties {

	private Map<String, String> queues;
}
