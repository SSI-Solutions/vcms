package com.adnovum.vcms.issuer.controller;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.issuer.server.controller.IssueApi;
import com.adnovum.vcms.genapi.issuer.server.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.issuer.server.dto.IssuStatus;
import com.adnovum.vcms.genapi.issuer.server.dto.IssuingRequest;
import com.adnovum.vcms.genapi.issuer.server.dto.IssuingResponse;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.service.AriesFacadeService;
import com.adnovum.vcms.issuer.service.ClaimService;
import com.adnovum.vcms.issuer.service.HolderService;
import com.adnovum.vcms.issuer.service.IssuingProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.adnovum.vcms.common.exception.BusinessReason.ERROR_CREDENTIAL_NOT_VALID;
import static com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState.ESTABLISHED;
import static com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState.RESPONDED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IssuerController implements IssueApi {

	private final IssuingProcessService issuingProcessService;

	private final AriesFacadeService ariesFacadeService;

	private final HolderService holderService;

	private final ClaimService claimService;

	@Override
	public ResponseEntity<List<CredentialDefinition>> getCredentialDefinitions() {
		return ResponseEntity.ok(ariesFacadeService.getCreatedCredentialDefinitions());
	}

	@Override
	public ResponseEntity<IssuingResponse> issueCredential(IssuingRequest issuingRequest) {
		UUID connectionId = issuingRequest.getConnectionId();
		AriesConnectionState connectionState = ariesFacadeService.getConnectionStateById(connectionId);
		IssuingResponse issuingResponse = new IssuingResponse();

		if (!(RESPONDED.equals(connectionState) || ESTABLISHED.equals(connectionState))) {
			issuingResponse.setStatus(IssuStatus.NO_CONNECTION);
			return ResponseEntity.ok(issuingResponse);
		}

		if (Boolean.FALSE.equals(ariesFacadeService.isCreatedCreDef(issuingRequest.getCredentialDefinitionId()))) {
			String msg = String.format("The given credential definition %s has not been created by this wallet and can " +
					"therefore not be used for issuing.", issuingRequest.getCredentialDefinitionId());
			throw new BusinessException(msg, ERROR_CREDENTIAL_NOT_VALID);
		}

		log.debug("issuing request received - sending offer over connection {}", connectionId);
		String credentialExchangeId = ariesFacadeService.offerCredential(connectionId,
				issuingRequest.getCredentialDefinitionId(), issuingRequest.getAttributes());

		log.debug("get or create holder for user id {}", issuingRequest.getUserId());
		Holder holder = holderService.createOrGetHolder(issuingRequest.getUserId());

		log.debug("creating issuing state and claims for connection {} and exchange {} and holder {}", connectionId,
				credentialExchangeId, holder.getUserId());
		IssuingProcess issuingProcess = issuingProcessService.createIssuingProcess(connectionId, credentialExchangeId, holder);
		claimService.persistClaims(issuingProcess, issuingRequest.getAttributes());

		issuingResponse.setStatus(issuingProcess.getProcessState().getIssuStatus());
		issuingResponse.setProcessId(issuingProcess.getId());
		return ResponseEntity.ok(issuingResponse);
	}

	@Override
	public ResponseEntity<IssuStatus> issuingState(UUID processId) {
		IssuingProcess issuingProcess = issuingProcessService.getIssuingProcessById(processId);
		return ResponseEntity.ok(issuingProcess.getProcessState().getIssuStatus());
	}
}
