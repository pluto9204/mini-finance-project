package com.service.processor.service;

import static com.service.processor.util.RabbitMQUtil.LOAN_RESULT_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanProcessProducer {

	
	private final RabbitTemplate rabbitTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(LoanProcessProducer.class);
	
	public void sendLoanResult(String resultMessage) {
		rabbitTemplate.convertAndSend(LOAN_RESULT_QUEUE ,resultMessage);
		logger.info("✅ Loan result sent to RabbitMQ: " + resultMessage);
	}
}
