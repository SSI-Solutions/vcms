package com.adnovum.vcms.issuer.datamodel.repository;


import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HolderRepositoryTest extends IssuerServerIntTestBase {

	@Test
	void shouldSaveAndFindEntitiesById() {
		String testHolderId = "shouldSaveAndFindEntitiesById";

		Holder holder = createTestHolder(testHolderId);
		holder = holderRepository.findById(holder.getId()).orElseThrow();

		assertThat(holder.getId()).isNotNull();
		assertThat(holder.getCtlCreTs()).isNotNull();
		assertThat(holder.getCtlCreUid()).isNotNull();
		assertThat(holder.getCtlModTs()).isNotNull();
		assertThat(holder.getCtlModUid()).isNotNull();
		assertThat(holder.getUserId()).isEqualTo(testHolderId);

		holderRepository.findByUserId(testHolderId).orElseThrow();
	}
}
