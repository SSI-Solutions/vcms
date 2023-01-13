package com.adnovum.vcms.connector.datamodel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ConnectionStateConverter implements AttributeConverter<ConnectionProcessState, String> {

	@Override
	public String convertToDatabaseColumn(ConnectionProcessState attribute) {
		return attribute != null ? attribute.name() : null;
	}

	@Override
	public ConnectionProcessState convertToEntityAttribute(String dbData) {
		return dbData != null ? ConnectionProcessState.valueOf(dbData) : null;
	}
}
