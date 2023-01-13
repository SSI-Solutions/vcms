package com.adnovum.vcms.common.datamodel.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

	@Test
	void shouldUseBaseEntity() {
		String test = "shouldUseBaseEntity";
		BaseEntity baseEntity = new BaseEntity();
		Date ctlTs = new Date();
		baseEntity.setCtlCreTs(ctlTs);
		baseEntity.setCtlCreUid(test);
		baseEntity.setCtlModTs(ctlTs);
		baseEntity.setCtlModUid(test);
		baseEntity.setId(UUID.randomUUID());
		assertThat(baseEntity.toString()).hasToString("BaseEntity{" +
				"id" + baseEntity.getId() +
				", ctlCreTs=" + baseEntity.getCtlCreTs() +
				", ctlCreUid='" + baseEntity.getCtlCreUid() + '\'' +
				", ctlModTs=" + baseEntity.getCtlModTs() +
				", ctlModUid='" + baseEntity.getCtlModUid() + '\'' +
				'}');
	}
}
