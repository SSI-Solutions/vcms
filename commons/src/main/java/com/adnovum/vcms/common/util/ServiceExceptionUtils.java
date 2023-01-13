package com.adnovum.vcms.common.util;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.common.exception.BusinessReason;

import java.util.UUID;

public class ServiceExceptionUtils {

	private ServiceExceptionUtils() {
	}

	public static BusinessException createNonExistentIdException(UUID id, BusinessReason reason) {
		return new BusinessException("Cannot resolve the given id = " + id, reason);
	}

	public static BusinessException createNonExistentIdException(BusinessReason reason, String... ids) {
		String msg = String.format("Cannot resolve the given ids = %s", String.join(",", ids));
		return new BusinessException(msg, reason);
	}
}
