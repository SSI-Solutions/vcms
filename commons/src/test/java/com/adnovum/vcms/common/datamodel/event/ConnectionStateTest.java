package com.adnovum.vcms.common.datamodel.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class ConnectionStateTest {

	@Test
	void checkMappings() {
		assertThat(ConnectionState.fromValue("init")).isEqualTo(ConnectionState.INIT);
		assertThat(ConnectionState.fromValue("start")).isEqualTo(ConnectionState.START);
		assertThat(ConnectionState.fromValue("invitation")).isEqualTo(ConnectionState.INVITATION);
		assertThat(ConnectionState.fromValue("request")).isEqualTo(ConnectionState.REQUEST_SENT);
		assertThat(ConnectionState.fromValue("response")).isEqualTo(ConnectionState.RESPONSE_RECEIVED);
		assertThat(ConnectionState.fromValue("completed")).isEqualTo(ConnectionState.COMPLETED);
		assertThat(ConnectionState.fromValue("active")).isEqualTo(ConnectionState.ACTIVE);
		assertThat(ConnectionState.fromValue("error")).isEqualTo(ConnectionState.ERROR);
		assertThat(ConnectionState.fromValue("abandoned")).isEqualTo(ConnectionState.ABANDONED);
	}
}
