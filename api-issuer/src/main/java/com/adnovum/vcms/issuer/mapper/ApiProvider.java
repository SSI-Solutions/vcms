package com.adnovum.vcms.issuer.mapper;

import com.adnovum.vcms.genapi.issuer.server.dto.CredentialResponse;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApiProvider implements MappingConfigurationProvider {

	@Override
	public void configure(ModelMapper modelMapper) {

		modelMapper.typeMap(IssuingProcess.class, CredentialResponse.class)
				.addMappings(mp -> {
							mp.map(IssuingProcess::getId, CredentialResponse::setCredentialId);
							mp.map(IssuingProcess::getConnectionId, CredentialResponse::setConnectionId);
							mp.map(IssuingProcess::getCtlCreTs, CredentialResponse::setIssuingDate);
						}
				);
	}
}
