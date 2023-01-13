package com.adnovum.vcms.verifier.service;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState;
import com.adnovum.vcms.verifier.repository.VerificationProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.adnovum.vcms.common.exception.BusinessReason.ERROR_VERIFICATION_STATE_NOT_EXISTENT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerificationProcessService {

	private final VerificationProcessRepository verificationProcessRepository;

	public VerificationProcess createVerificationProcess(String connectionId, String presentationExchangeId) {
		VerificationProcess verificationProcess = new VerificationProcess();
		verificationProcess.setConnectionId(connectionId);
		verificationProcess.setPresentationExchangeId(presentationExchangeId);
		verificationProcess.setStatus(VerificationProcessState.VERIFICATION_STATE_CREATED);
		verificationProcessRepository.save(verificationProcess);
		log.debug("Verification process {} in state {}", connectionId, verificationProcess.getStatus());
		return verificationProcess;
	}

	public VerificationProcess updateVerificationProcessState(VerificationProcess verificationProcess,
			VerificationProcessState verificationProcessState) {
		if (!verificationProcessState.equals(verificationProcess.getStatus())) {
			verificationProcess.setStatus(verificationProcessState);
			verificationProcessRepository.save(verificationProcess);
		}
		log.debug("Verification process {} in state {}", verificationProcess.getId(), verificationProcess.getStatus());
		return verificationProcess;
	}

	public Optional<VerificationProcess> getVerificationProcessByPresentationExchangeId(String presentationExchangeId) {
		return verificationProcessRepository.findByPresentationExchangeId(presentationExchangeId);
	}

	public VerificationProcess getVerificationProcessById(UUID id) {
		return verificationProcessRepository.findById(id).orElseThrow(() ->
				new BusinessException("Cannot resolve the given {id} = " + id, ERROR_VERIFICATION_STATE_NOT_EXISTENT));
	}

	public Boolean doesProcessExist(UUID id) {
		return verificationProcessRepository.existsVerificationProcessById(id);
	}

	public void deleteByProcess(VerificationProcess verificationProcess) {
		verificationProcessRepository.deleteById(verificationProcess.getId());
	}
}
