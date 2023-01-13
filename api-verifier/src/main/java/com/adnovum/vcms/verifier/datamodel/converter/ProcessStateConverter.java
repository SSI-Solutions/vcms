package com.adnovum.vcms.verifier.datamodel.converter;

import com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProcessStateConverter implements AttributeConverter<VerificationProcessState, String> {

	@Override
	public String convertToDatabaseColumn(VerificationProcessState attribute) {
		return attribute != null ? attribute.name() : null;
	}

	@Override
	public VerificationProcessState convertToEntityAttribute(String dbData) {
		return dbData != null ? VerificationProcessState.valueOf(dbData) : null;
	}
}
