package com.adnovum.vcms.common.datamodel.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class PresentationExchangeEvent implements Serializable {

	private UUID connectionId;

	private String presentationExchangeId;

	private PresentationExchangeState presentationExchangeState;

	private Boolean verified;

	private Map<String, String> revealedAttributes;
}
