package com.service.processor.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.service.common.entity.CreditMasterEntity;
import com.service.common.repository.CreditMasterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditScoreTxService {
	
	private final CreditMasterRepository creditMasterRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public String callCreditScore(Long LoanId, String userId, String requestStr) {
		String ResponseStr = "";

		CreditMasterEntity creditMasterEntity = new CreditMasterEntity();
		creditMasterEntity.setLoanId(LoanId);
		creditMasterEntity.setUserId(userId);
		creditMasterEntity.setRequestPayload(requestStr);
		creditMasterEntity = creditMasterRepository.save(creditMasterEntity);

		RestTemplate restTemplate = new RestTemplate();
		ResponseStr = restTemplate.postForObject("http://loan-processor:8083/internal/credit/evaluateCreditScore", requestStr, String.class);

		creditMasterEntity.setResponsePayload(ResponseStr);
		creditMasterEntity.setReponseTimestamp(LocalDateTime.now());
		creditMasterRepository.save(creditMasterEntity);
		
		return ResponseStr;
	}
}
