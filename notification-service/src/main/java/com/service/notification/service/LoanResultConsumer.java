package com.service.notification.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.notification.dto.LoanResultEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanResultConsumer {	
	private final NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(LoanResultConsumer.class);
	
    @RabbitListener(queues = "#{T(com.service.notification.util.RabbitMQUtil).LOAN_RESULT_QUEUE}")
    public void receiveLoanResult(String resultMessage) {
    	ObjectMapper objectMapper = new ObjectMapper();
    	JSONObject loanResultObject = (JSONObject) objectMapper.convertValue(resultMessage, JSONObject.class);
    	logger.info("Loan Result : " + loanResultObject.toString());
    	
    	String userId = loanResultObject.getString("userId");
    	String result = loanResultObject.getString("result");
    	
    	if("APPROVED".equals(result)) {
    		notificationService.sendNotificationMessage(userId.concat("님 ")
    				.concat("대출심사결과 : ")
    				.concat(result)
    				.concat(" 으로 대출심사 및 실행 완료되었습니다."));
    	} else {
    		notificationService.sendNotificationMessage(userId.concat("님 ")
    				.concat("대출심사결과 : ")
    				.concat(result)
    				.concat(" 으로 대출심사 거절되었습니다."));
    	}
    }
}
