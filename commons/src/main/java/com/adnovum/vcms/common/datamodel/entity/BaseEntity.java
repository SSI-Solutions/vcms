package com.adnovum.vcms.common.datamodel.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class BaseEntity {

	@Id
	@GeneratedValue
	@Type(type = "uuid-char")
	private UUID id;

	@CreatedDate
	@Column(name = "ctl_cre_ts", updatable = false)
	private Date ctlCreTs;

	@CreatedBy
	@Column(name = "ctl_cre_uid", updatable = false)
	@Size(max = 40)
	private String ctlCreUid;

	@LastModifiedDate
	@Column(name = "ctl_mod_ts")
	private Date ctlModTs;

	@LastModifiedBy
	@Column(name = "ctl_mod_uid")
	@Size(max = 40)
	private String ctlModUid;

	@Override
	public String toString() {
		return "BaseEntity{" +
				"id" + id +
				", ctlCreTs=" + ctlCreTs +
				", ctlCreUid='" + ctlCreUid + '\'' +
				", ctlModTs=" + ctlModTs +
				", ctlModUid='" + ctlModUid + '\'' +
				'}';
	}
}
