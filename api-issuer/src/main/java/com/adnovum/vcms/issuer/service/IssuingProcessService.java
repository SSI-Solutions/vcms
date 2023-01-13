package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import com.adnovum.vcms.issuer.datamodel.repository.IssuingProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.adnovum.vcms.common.exception.BusinessReason.ERROR_ISSUING_STATE_NOT_EXISTENT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class IssuingProcessService {

	private final IssuingProcessRepository issuingProcessRepository;

	public IssuingProcess createIssuingProcess(UUID connectionId, String credentialExchangeId, Holder holder) {
		IssuingProcess issuingProcess = new IssuingProcess();
		issuingProcess.setConnectionId(connectionId);
		issuingProcess.setProcessState(ProcessState.PROCESS_CREATED);
		issuingProcess.setCredentialExchangeId(credentialExchangeId);
		issuingProcess.setHolder(holder);
		issuingProcessRepository.save(issuingProcess);
		log.debug("Issuing {} in process state {}", credentialExchangeId, issuingProcess.getProcessState());
		return issuingProcess;
	}

	public IssuingProcess updateProcessState(UUID processId, ProcessState processState) {
		IssuingProcess issuingProcess = getIssuingProcessById(processId);
		if (!processState.equals(issuingProcess.getProcessState())) {
			issuingProcess.setProcessState(processState);
			issuingProcessRepository.save(issuingProcess);
		}
		log.debug("Issuing {} in process state {}", issuingProcess.getCredentialExchangeId(), issuingProcess.getProcessState());
		return issuingProcess;
	}

	public IssuingProcess updateRevocationState(UUID processId, RevocationState revocationState) {
		IssuingProcess issuingProcess = getIssuingProcessById(processId);
		if (!revocationState.equals(issuingProcess.getRevocationState())) {
			issuingProcess.setRevocationState(revocationState);
			issuingProcessRepository.save(issuingProcess);
		}
		log.debug("Issuing {} in revocation state {}", issuingProcess.getCredentialExchangeId(), issuingProcess.getProcessState());
		return issuingProcess;
	}

	public Optional<IssuingProcess> getOptionalByCredentialExchangeId(String presentationExchangeId) {
		return issuingProcessRepository.findByCredentialExchangeId(presentationExchangeId);
	}

	public IssuingProcess getIssuingProcessById(UUID processId) {
		return issuingProcessRepository.findById(processId).orElseThrow(() ->
				new BusinessException("Cannot resolve the given {processId} = " + processId, ERROR_ISSUING_STATE_NOT_EXISTENT));
	}

	public Boolean hasNoActiveCredentials(Holder holder) {
		return (issuingProcessRepository.countIssuingProcessByHolderAndRevocationState(holder, RevocationState.ISSUED) == 0);
	}
}
