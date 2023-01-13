package com.adnovum.vcms.verifier.datamodel.entity;

import com.adnovum.vcms.common.datamodel.entity.BaseEntity;
import com.adnovum.vcms.verifier.datamodel.converter.ProcessStateConverter;
import com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Getter @Setter
@Table(name = "verification_process")
public class VerificationProcess extends BaseEntity {

	@NotBlank
	@Column(name = "connection_id", nullable = false)
	private String connectionId;

	@NotBlank
	@Column(name = "presentation_exchange_id", nullable = false, unique = true)
	private String presentationExchangeId;

	@Convert(converter = ProcessStateConverter.class)
	@Column(name = "status", nullable = false)
	private VerificationProcessState status;
}
