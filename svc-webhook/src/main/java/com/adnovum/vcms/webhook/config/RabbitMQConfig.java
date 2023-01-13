package com.adnovum.vcms.webhook.config;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	static final String DEAD_LETTER_EXCHANGE_NAME = "deadLetterExchange";

	static final String DEAD_LETTER_QUEUE_NAME = "deadLetterQueue";

	static final int MESSAGE_TTL_IN_MS = 60000;

	static final Map<String, Object> QUEUE_ARGUMENTS = Map.of("x-message-ttl", MESSAGE_TTL_IN_MS, "x-dead-letter-exchange",
			DEAD_LETTER_EXCHANGE_NAME, "x-dead-letter-routing-key", DEAD_LETTER_EXCHANGE_NAME);


	@Value("${vcms.rabbitmq.connection.exchange-name}")
	String connectionExchangeName;

	@Value("${vcms.rabbitmq.connection.connector-queue}")
	String connectorConnectionQueueName;

	@Value("${vcms.rabbitmq.presentation.exchange-name}")
	String presentationExchangeName;

	@Value("${vcms.rabbitmq.presentation.verifier-queue}")
	String verifierPresentationQueueName;

	@Value("${vcms.rabbitmq.credential.exchange-name}")
	String credentialExchangeName;

	@Value("${vcms.rabbitmq.credential.issuer-queue}")
	String issuerCredentialQueueName;

	@Bean
	public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public DirectExchange connectionExchange() {
		return new DirectExchange(connectionExchangeName);
	}

	@Bean
	public Queue connectorConnectionQueue() {
		return new Queue(connectorConnectionQueueName, true, false, false, QUEUE_ARGUMENTS);
	}

	@Bean
	public Binding bindingControllerConnectionTopic(DirectExchange connectionExchange, Queue connectorConnectionQueue) {
		return BindingBuilder.bind(connectorConnectionQueue)
				.to(connectionExchange).with(connectionExchange.getName());
	}

	// verification exchange

	@Bean
	public DirectExchange presentationExchange() {
		return new DirectExchange(presentationExchangeName, true, false);
	}

	@Bean
	public Queue verifierPresentationQueue() {
		return new Queue(verifierPresentationQueueName, true, false, false, QUEUE_ARGUMENTS);
	}

	@Bean
	public Binding bindingVerificationTopic(DirectExchange presentationExchange, Queue verifierPresentationQueue) {
		return BindingBuilder.bind(verifierPresentationQueue)
				.to(presentationExchange).with(presentationExchange.getName());
	}

	// issuing exchange

	@Bean
	public DirectExchange credentialExchange() {
		return new DirectExchange(credentialExchangeName, true, false);
	}

	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME, true, false);
	}

	@Bean
	public Queue deadLetterQueue() {
		return new Queue(DEAD_LETTER_QUEUE_NAME, true, false, false);
	}

	@Bean
	public Binding deadLetterBinding(DirectExchange deadLetterExchange, Queue deadLetterQueue) {
		return BindingBuilder.bind(deadLetterQueue)
				.to(deadLetterExchange).with(deadLetterExchange.getName());
	}


	@Bean
	public Queue issuerCredentialQueue() {
		return new Queue(issuerCredentialQueueName, true, false, false, QUEUE_ARGUMENTS);
	}

	@Bean
	public Binding bindingIssuerCredentialTopic(DirectExchange credentialExchange, Queue issuerCredentialQueue) {
		return BindingBuilder.bind(issuerCredentialQueue)
				.to(credentialExchange).with(credentialExchange.getName());
	}
}
