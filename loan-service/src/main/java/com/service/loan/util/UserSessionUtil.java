package com.service.loan.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.service.common.session.UserSession;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSessionUtil {
	private final RedisTemplate<String, Object> redisTemplate;
	
	public UserSession getUserSession (String userId) {
		String redisKey = "userId:" + userId;
		UserSession userSession = (UserSession) redisTemplate.opsForValue().get(redisKey);
		
		if(userSession == null) {
			throw new RuntimeException("존재하지 않는 유저입니다.");
		}
		
		return userSession;
	}
}
