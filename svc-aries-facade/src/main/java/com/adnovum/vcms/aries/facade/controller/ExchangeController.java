package com.adnovum.vcms.aries.facade.controller;

import com.adnovum.vcms.aries.facade.service.AriesService;
import com.adnovum.vcms.genapi.aries.facade.server.controller.ExchangesApi;
import com.adnovum.vcms.genapi.aries.facade.server.dto.IssuingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExchangeController implements ExchangesApi {

	private final AriesService ariesService;

	@Override
	public ResponseEntity<IssuingResponse> issueCredential(UUID exchangeId) {
		return ResponseEntity.ok(ariesService.issueCredentialRecord(String.valueOf(exchangeId), "comment"));
	}
}
