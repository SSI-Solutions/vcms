package com.adnovum.vcms.issuer.datamodel.entity;

import com.adnovum.vcms.common.datamodel.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "claim", uniqueConstraints = @UniqueConstraint(columnNames = { "claim_key", "issuing_process_id" }))
@Entity
@Getter
@Setter
public class Claim extends BaseEntity {

	@NotBlank
	@Column(name = "claim_key", nullable = false)
	private String claimKey;

	@NotNull
	@Column(name = "claim_value", nullable = false)
	private String claimValue;

	@ManyToOne
	@JoinColumn(name = "issuing_process_id", nullable = false, foreignKey = @ForeignKey(name = "fk_claim_in_process"))
	private IssuingProcess issuingProcess;

	public Claim(String claimKey, String claimValue) {
		setClaimKey(claimKey);
		setClaimValue(claimValue);
	}

	public Claim() {}
}
