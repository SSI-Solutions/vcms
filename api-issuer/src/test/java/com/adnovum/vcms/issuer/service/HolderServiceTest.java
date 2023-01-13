package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.genapi.issuer.server.dto.HolderResponse;
import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class HolderServiceTest extends IssuerServerIntTestBase {

	@Test
	void shouldGetHolderByUserId() {
		String test = "shouldGetHolderByEmail";

		Holder holder = createTestHolder(test);
		holder = holderService.getHolderById(holder.getId());

		assertThat(holder.getUserId()).isEqualTo(test);
	}

	@Test
	void shouldFailGetHolderByUserId() {
		UUID randomId = UUID.randomUUID();

		try {
			holderService.getHolderById(randomId);
			fail("Should have thrown an exception");
		}
		catch (Exception e) {
			assertThat(e.getClass()).isEqualTo(BusinessException.class);
			assertThat(e.getMessage()).isEqualTo("Cannot resolve the given id = " + randomId);
		}
	}

	@Test
	void shouldDeleteHolder() {
		String test = "shouldDeleteHolder";
		Holder holder = createTestHolder(test);

		List<HolderResponse> holderResponseList = holderService.getHolders();
		int numberOfAttributes = holderResponseList.size();

		holderService.deleteHolderById(holder.getId());

		holderResponseList = holderService.getHolders();
		assertThat(holderResponseList).hasSize(numberOfAttributes - 1);
	}

	@Test
	void shouldGetHolders() {
		String test = "shouldGetHolders";
		Holder holder1 = createTestHolder(test);
		Holder holder2 = createTestHolder(test + "-2");

		List<HolderResponse> holderResponseList = holderService.getHolders();

		assertThat(holderResponseList)
				.contains(modelMapper.map(holder1, HolderResponse.class))
				.contains(modelMapper.map(holder2, HolderResponse.class));
	}

	@Test
	void shouldCreateHolder() {
		String test = "shouldCreateHolder";
		Holder holderRequest = new Holder();
		holderRequest.setUserId(test);

		Holder holder = holderService.createOrGetHolder(holderRequest.getUserId());
		assertThat(holder.getId()).isNotNull();
		assertThat(holder.getUserId()).isEqualTo(test);
	}
}
