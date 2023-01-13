package com.adnovum.vcms.issuer.datamodel.entity;

import com.adnovum.vcms.common.datamodel.entity.BaseEntity;
import com.adnovum.vcms.issuer.datamodel.converter.ProcessStateConverter;
import com.adnovum.vcms.issuer.datamodel.converter.RevocationStateConverter;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "issuing_process")
public class IssuingProcess extends BaseEntity {

	@Column(name = "connection_id", nullable = false)
	private UUID connectionId;

	@NotBlank
	@Column(name = "credential_exchange_id", nullable = false, unique = true)
	private String credentialExchangeId;

	@Convert(converter = ProcessStateConverter.class)
	@Column(name = "process_state", nullable = false)
	private ProcessState processState;

	@ManyToOne
	@JoinColumn(name = "holder_id")
	private Holder holder;

	@Convert(converter = RevocationStateConverter.class)
	@Column(name = "revocation_state")
	private RevocationState revocationState;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "issuingProcess")
	private List<Claim> claims;
}
