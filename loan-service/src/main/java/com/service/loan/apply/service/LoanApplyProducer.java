package com.service.loan.apply.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.common.code.LoanCodeConstant;
import com.service.common.entity.LoanMasterEntity;
import com.service.common.repository.LoanMasterRepository;
import com.service.loan.apply.dto.RequestLoanDTO;
import com.service.loan.util.KafkaUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanApplyProducer {
	
	private final KafkaUtil kafkaUtil; 
	
	private final LoanMasterRepository requestLoanRepository;
	
	@Transactional
	public Map<String, Object> requestLoan(RequestLoanDTO requestLoanDTO) {
		String key = requestLoanDTO.getUserId();
		
		//대출신청 정보 저장
		LoanMasterEntity LoanMasterEntity = new LoanMasterEntity();
		LoanMasterEntity.setUserId(requestLoanDTO.getUserId());
		LoanMasterEntity.setPurpose(requestLoanDTO.getPurpose());
		LoanMasterEntity.setLoanAmount(requestLoanDTO.getAmount());
		LoanMasterEntity.setMonthlyIncome(requestLoanDTO.getMonthlyIncome());
		LoanMasterEntity.setLoanState(LoanCodeConstant.LoanApplyState.REQEUSTED.toString());
		
		LoanMasterEntity = requestLoanRepository.save(LoanMasterEntity);
		requestLoanDTO.setLoanId(LoanMasterEntity.getLoanId()); //Loan ID 셋팅
		
		kafkaUtil.sendMessageWithKey("loan-apply",key,requestLoanDTO); //대출신청 요청
		
		return buildResponse("00", "대출신청완료");
	}
	

    private Map<String, Object> buildResponse(String resCode, String resMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("res_code", resCode);
        response.put("res_message", resMessage);
        return response;
    }
}
