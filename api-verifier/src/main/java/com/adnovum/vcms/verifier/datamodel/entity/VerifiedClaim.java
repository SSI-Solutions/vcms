package com.adnovum.vcms.verifier.datamodel.entity;

import com.adnovum.vcms.common.datamodel.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
@Table(name = "verified_claim")
public class VerifiedClaim extends BaseEntity {

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "verification_process_id", nullable = false, foreignKey = @ForeignKey(name = "fk_claim_of_process"))
	private VerificationProcess verificationProcess;

	@NotBlank
	@Column(name = "claim_key", nullable = false)
	private String claimKey;

	@NotNull
	@Column(name = "claim_value", nullable = false)
	private String claimValue;
}
