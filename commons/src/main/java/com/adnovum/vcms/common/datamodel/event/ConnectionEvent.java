package com.adnovum.vcms.common.datamodel.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@RequiredArgsConstructor
@Getter @Setter
public class ConnectionEvent implements Serializable {

	private UUID connectionId;

	private ConnectionState connectionState;
}
