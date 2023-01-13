package com.adnovum.vcms.connector.datamodel;

import com.adnovum.vcms.common.datamodel.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Connection extends BaseEntity {

	@Column(name = "connection_id", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID connectionId;

	@Convert(converter = ConnectionStateConverter.class)
	@Column(name = "connection_state", nullable = false)
	private ConnectionProcessState connectionState;
}
