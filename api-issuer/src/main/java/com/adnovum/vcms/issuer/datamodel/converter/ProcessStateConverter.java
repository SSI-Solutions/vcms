package com.adnovum.vcms.issuer.datamodel.converter;

import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProcessStateConverter implements AttributeConverter<ProcessState, String> {

	@Override
	public String convertToDatabaseColumn(ProcessState attribute) {
		return attribute != null ? attribute.getValue() : null;
	}

	@Override
	public ProcessState convertToEntityAttribute(String dbData) {
		return dbData != null ? ProcessState.fromValue(dbData) : null;
	}
}
