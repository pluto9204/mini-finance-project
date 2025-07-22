package com.service.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	public void sendNotificationMessage(String msg) {
		logger.info("send Notify Message : " + msg);
	}
}
