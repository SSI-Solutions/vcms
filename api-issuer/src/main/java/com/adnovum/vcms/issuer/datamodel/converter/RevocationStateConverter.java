package com.adnovum.vcms.issuer.datamodel.converter;

import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class RevocationStateConverter implements AttributeConverter<RevocationState, String> {

	@Override
	public String convertToDatabaseColumn(RevocationState attribute) {
		return attribute != null ? attribute.getValue() : null;
	}

	@Override
	public RevocationState convertToEntityAttribute(String dbData) {
		return dbData != null ? RevocationState.fromValue(dbData) : null;
	}
}
