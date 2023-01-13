package com.adnovum.vcms.aries.facade;

import java.util.UUID;

import com.adnovum.vcms.aries.facade.service.AcaPyProperties;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "test" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AriesFacadeIntBase {

	protected String schemaID = "WgWxqztrNooG92RXvxSTWv:2:schema_name:1.0";
	protected String credDefID = "Lwj2sRbVtgzMH5sLH2VyPF:3:CL:55093:mockCredDef";
	protected String invitationURL = "https://iam.not.a.valid.url:9876";
	protected UUID testUUID = UUID.randomUUID();

	@Autowired
	protected AcaPyProperties acaPyProperties;
}
