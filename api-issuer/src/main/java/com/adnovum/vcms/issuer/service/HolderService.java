package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.common.exception.BusinessReason;
import com.adnovum.vcms.common.util.ServiceExceptionUtils;
import com.adnovum.vcms.genapi.issuer.server.dto.CredentialResponse;
import com.adnovum.vcms.genapi.issuer.server.dto.HolderResponse;
import com.adnovum.vcms.issuer.datamodel.entity.Holder;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.repository.HolderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.adnovum.vcms.common.exception.BusinessReason.ERROR_DATA_INVALID_CONSTELLATION;

@Service
@Transactional
@RequiredArgsConstructor
public class HolderService {

	private final HolderRepository holderRepository;

	private final ModelMapper modelMapper;

	private final IssuingProcessService issuingProcessService;

	static final String ANONYMOUS_USER_ID = "Anonymous";

	public Holder createOrGetHolder(String userId) {
		if (StringUtils.isBlank(userId)) {
			userId = ANONYMOUS_USER_ID;
		}
		Optional<Holder> holderOptional = holderRepository.findByUserId(userId);
		if (holderOptional.isPresent()) {
			return holderOptional.get();
		}
		else {
			Holder holder = new Holder();
			holder.setUserId(userId);
			return holderRepository.save(holder);
		}
	}

	public List<HolderResponse> getHolders() {
		List<Holder> holders = Streamable.of(holderRepository.findAll()).toList();
		return holders.stream().map(e -> modelMapper.map(e, HolderResponse.class)).collect(Collectors.toList());
	}

	public void deleteHolderById(UUID id) {
		Holder holder = getHolderById(id);
		if (Boolean.TRUE.equals(issuingProcessService.hasNoActiveCredentials(holder))) {
			holderRepository.delete(holder);
		}
		else {
			throw new BusinessException("Holder can not be deleted because it has issued credentials.",
					ERROR_DATA_INVALID_CONSTELLATION);
		}
	}

	public List<CredentialResponse> getCredentialsForHolderId(UUID holderId) {
		Holder holder = getHolderById(holderId);
		List<IssuingProcess> issuingProcesses = holder.getIssuingProcesses();
		return issuingProcesses.stream().map(e -> modelMapper.map(e, CredentialResponse.class)).collect(Collectors.toList());
	}

	public Holder getHolderById(UUID id) {
		return holderRepository.findById(id).orElseThrow(() ->
				ServiceExceptionUtils.createNonExistentIdException(id, BusinessReason.ERROR_HOLDER_NOT_EXISTENT));
	}
}
