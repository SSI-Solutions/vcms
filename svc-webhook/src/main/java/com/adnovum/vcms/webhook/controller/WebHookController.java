package com.adnovum.vcms.webhook.controller;

import com.adnovum.vcms.genapi.webhook.server.controller.TopicApi;
import com.adnovum.vcms.genapi.webhook.server.dto.ConnRecord;
import com.adnovum.vcms.genapi.webhook.server.dto.V10CredentialExchange;
import com.adnovum.vcms.genapi.webhook.server.dto.V10PresentationExchange;
import com.adnovum.vcms.webhook.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebHookController implements TopicApi {

	private final WebhookService webHookService;

	@Override
	public ResponseEntity<Void> topicConnections(ConnRecord connRecord) {
		log.debug("Received connection event: {}", connRecord);
		webHookService.connectionEventFromAcaPy(connRecord);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> topicIssueCredentials(V10CredentialExchange v10CredentialExchange) {
		log.debug("Received issue-credential event: {}", v10CredentialExchange);
		webHookService.issueCredentialsEventFromAcaPy(v10CredentialExchange);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> topicPresentProof(V10PresentationExchange presentationExchange) {
		log.debug("Received present-proof event: {}", presentationExchange);
		webHookService.presentProofEventFromAcaPy(presentationExchange);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(
			value = "/topic/**",
			method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PATCH  })
	@ResponseBody
	public String allFallback(HttpServletRequest request) {
		log.info("Unprocessed request: " + request.getMethod() + " " + request.getRequestURL());
		return "Fallback for All Requests";
	}
}
