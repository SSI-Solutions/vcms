package com.adnovum.vcms.issuer.service;


import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.adnovum.vcms.common.service.VcmsFeatureProperties;
import com.adnovum.vcms.issuer.datamodel.entity.Claim;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClaimService {

	private final ClaimRepository claimRepository;

	private final VcmsFeatureProperties vcmsFeaturesProperties;

	public void persistClaims(IssuingProcess issuingProcess, Map<String, String> claims) {
		if (!vcmsFeaturesProperties.getStoreClaims()) {
			log.debug("Claims will not be persisted, the feature is turned off");
			return;
		}

		Set<Claim> claimsToSave = claims.entrySet().stream()
				.filter(e -> e.getKey() != null)
				.map(e -> {
					if (e.getValue() == null) {
						e.setValue("");
					}
					return e;
				})
				.map(e -> new Claim(e.getKey(), e.getValue()))
				.collect(Collectors.toSet());

		claimsToSave.forEach(x -> x.setIssuingProcess(issuingProcess));
		claimRepository.saveAll(claimsToSave);
	}
}
