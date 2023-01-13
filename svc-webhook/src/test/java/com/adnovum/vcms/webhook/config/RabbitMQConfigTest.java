package com.adnovum.vcms.webhook.config;

import com.adnovum.vcms.webhook.WebhookIntBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigTest extends WebhookIntBase {

	@Autowired
	RabbitMQConfig rabbitMQConfig;

	@Test
	void configValues() {
		assertThat(rabbitMQConfig.connectionExchangeName).isEqualTo("connectionExchange");
		assertThat(rabbitMQConfig.connectionExchange()).isNotNull();
		assertThat(rabbitMQConfig.connectorConnectionQueueName).isEqualTo("connectorConnectionQueue");
		assertThat(rabbitMQConfig.connectorConnectionQueue()).isNotNull();

		assertThat(rabbitMQConfig.credentialExchangeName).isEqualTo("credentialExchange");
		assertThat(rabbitMQConfig.credentialExchange()).isNotNull();
		assertThat(rabbitMQConfig.issuerCredentialQueueName).isEqualTo("issuerCredentialQueue");
		assertThat(rabbitMQConfig.issuerCredentialQueue()).isNotNull();

		assertThat(rabbitMQConfig.presentationExchangeName).isEqualTo("presentationExchange");
		assertThat(rabbitMQConfig.presentationExchange()).isNotNull();
		assertThat(rabbitMQConfig.verifierPresentationQueueName).isEqualTo("verifierPresentationQueue");
		assertThat(rabbitMQConfig.verifierPresentationQueue()).isNotNull();
	}
}
