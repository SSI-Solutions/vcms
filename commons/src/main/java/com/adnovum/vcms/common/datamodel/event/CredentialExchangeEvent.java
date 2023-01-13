package com.adnovum.vcms.common.datamodel.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class CredentialExchangeEvent implements Serializable {
	//V10CredentialExchange

	private UUID connectionId;

	private String credentialExchangeId;

	private CredentialExchangeState credentialExchangeState;
}
