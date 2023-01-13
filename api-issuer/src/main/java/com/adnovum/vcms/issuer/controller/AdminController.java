package com.adnovum.vcms.issuer.controller;


import com.adnovum.vcms.genapi.issuer.server.controller.AdminApi;
import com.adnovum.vcms.genapi.issuer.server.dto.CredentialResponse;
import com.adnovum.vcms.genapi.issuer.server.dto.HolderResponse;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import com.adnovum.vcms.issuer.service.AriesFacadeService;
import com.adnovum.vcms.issuer.service.HolderService;
import com.adnovum.vcms.issuer.service.IssuingProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

	private final HolderService holderService;

	private final IssuingProcessService issuingProcessService;

	private final AriesFacadeService ariesFacadeService;

	/**
	 * GET /admin/holders/{holderId}/credentials : Get all credentials for a given holder
	 */
	@Override
	public ResponseEntity<List<CredentialResponse>> getCredentialsFromHolder(UUID holderId) {
		return new ResponseEntity<>(holderService.getCredentialsForHolderId(holderId), HttpStatus.OK);
	}

	/**
	 * GET /admin/holders : Get all holders
	 */
	@Override
	public ResponseEntity<List<HolderResponse>> getHolders() {
		return new ResponseEntity<>(holderService.getHolders(), HttpStatus.OK);
	}

	/**
	 * DELETE /admin/holders/{holderId} : Delete Holder
	 */
	@Override
	public ResponseEntity<Void> deleteHolder(UUID holderId) {
		holderService.deleteHolderById(holderId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * PUT admin/credentials/{id}/revoke : Revoke a certain credential
	 */
	@Override
	public ResponseEntity<Void> revokeCredential(UUID processId) {
		IssuingProcess issuingProcess = issuingProcessService.getIssuingProcessById(processId);
		ariesFacadeService.revokeCredential(issuingProcess.getCredentialExchangeId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
