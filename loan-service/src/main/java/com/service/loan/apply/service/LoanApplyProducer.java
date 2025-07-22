package com.service.loan.apply.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.service.loan.apply.dto.RequestLoanDTO;
import com.service.loan.util.KafkaUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanApplyProducer {
	
	private final KafkaUtil kafkaUtil; 
	

	public Map<String, Object> requestLoan(RequestLoanDTO requestLoanDTO) {	
		String key = requestLoanDTO.getUserId();
		
		kafkaUtil.sendMessageWithKey("loan-apply",key,requestLoanDTO);
		
		return buildResponse("00", "대출신청완료");
	}
	

    private Map<String, Object> buildResponse(String resCode, String resMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("res_code", resCode);
        response.put("res_message", resMessage);
        return response;
    }
}
