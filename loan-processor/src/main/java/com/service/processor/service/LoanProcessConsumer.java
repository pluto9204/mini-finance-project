package com.service.processor.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.service.loan.apply.dto.RequestLoanDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanProcessConsumer {
	
	private final LoanApplyService loanApplyService;
    private final LoanProcessProducer loanProcessProducer;
	private static final Logger logger = LoggerFactory.getLogger(LoanProcessConsumer.class);
	
	@KafkaListener(topics="loan-apply", groupId="loan-processor-group")
	public void comsumeLoan(@Payload RequestLoanDTO requestLoanDTO, @Header(KafkaHeaders.RECEIVED_KEY) String key) {
		try {
	        logger.info("Kafka Received → key: " + key + ", value: " + requestLoanDTO);
			String loanResult = loanApplyService.processLoan(requestLoanDTO);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", requestLoanDTO.getUserId());
			jsonObject.put("result", loanResult);
			jsonObject.put("amount", requestLoanDTO.getAmount());
			jsonObject.put("reason", loanResult);
			
	        loanProcessProducer.sendLoanResult(jsonObject.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
