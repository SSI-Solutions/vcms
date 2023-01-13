package com.adnovum.vcms.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	private final AmqpAdmin rabbitAdmin;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		rabbitAdmin.initialize();
	}
}
