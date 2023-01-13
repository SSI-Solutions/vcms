package com.adnovum.vcms.connector.service;

import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AriesFacadeService {

	private final AriesFacadeClient ariesFacadeClient;

	public AriesConnectionInvitation getInvitation() {
		return ariesFacadeClient.getInvitation();
	}
}
