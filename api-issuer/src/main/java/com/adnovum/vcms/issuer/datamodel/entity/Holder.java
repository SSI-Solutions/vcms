package com.adnovum.vcms.issuer.datamodel.entity;

import com.adnovum.vcms.common.datamodel.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "holder")
public class Holder extends BaseEntity {

	// JWT sub subject mapped to ID, coming from Principal
	@NotBlank
	@Column(unique = true, nullable = false)
	private String userId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "holder")
	private List<IssuingProcess> issuingProcesses;
}
