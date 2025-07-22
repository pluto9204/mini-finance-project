package com.service.notification.util;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQUtil {
	public static final String LOAN_RESULT_QUEUE = "loan-result";
	
	@Bean
	public Queue loanResultQueue() {
		return new Queue(LOAN_RESULT_QUEUE, true);
	}
}
