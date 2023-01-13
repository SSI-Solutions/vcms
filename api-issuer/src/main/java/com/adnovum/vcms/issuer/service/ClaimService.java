package com.adnovum.vcms.issuer.service;


import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.adnovum.vcms.issuer.datamodel.entity.Claim;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClaimService {

	private final ClaimRepository claimRepository;

	public void createClaims(IssuingProcess issuingProcess, Map<String, String> claims) {
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
