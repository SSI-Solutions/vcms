package com.adnovum.vcms.verifier.service;

import java.util.List;

import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.entity.VerifiedClaim;
import com.adnovum.vcms.verifier.repository.VerifiedClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerifiedClaimService {

	private final VerifiedClaimRepository verifiedClaimRepository;

	public VerifiedClaim createAndStoreVerifiedCredential(VerificationProcess verificationProcess, String claim, String value) {
		if (null == claim) {
			return new VerifiedClaim();
		}
		if (value == null) {
			value = "";
		}
		VerifiedClaim verifiedClaim = new VerifiedClaim();
		verifiedClaim.setClaimValue(value);
		verifiedClaim.setClaimKey(claim);
		verifiedClaim.setVerificationProcess(verificationProcess);
		return verifiedClaimRepository.save(verifiedClaim);
	}

	public List<VerifiedClaim> getVerifiedClaimsByVerificationProcess(VerificationProcess verificationProcess) {
		return verifiedClaimRepository.findAllByVerificationProcess(verificationProcess);
	}
}
