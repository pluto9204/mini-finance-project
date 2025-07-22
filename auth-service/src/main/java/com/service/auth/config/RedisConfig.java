package com.service.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
public class RedisConfig {
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {// RedisConnectionFactory :	Redis 서버와의 연결을 담당
		RedisTemplate<String, Object> template = new RedisTemplate(); //Redis에서 읽고쓰는 객체
		template.setConnectionFactory(connectionFactory);
		
		StringRedisSerializer keySerializer = new StringRedisSerializer(); //	문자열 키 직렬화 해주는 객체
		Jackson2JsonRedisSerializer<Object> valueSerializer = jackson2JsonRedisSerializer(); //객체를 JSON 문자열로 직렬화/역직렬화 해주는 객체
		
		template.setKeySerializer(keySerializer);
		template.setValueSerializer(valueSerializer);
		template.setHashKeySerializer(keySerializer);
		template.setHashValueSerializer(valueSerializer);
		
		template.afterPropertiesSet();
		return template;
	}
	
	private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class); //JSON 직렬화 도구 생성
		ObjectMapper mapper = new ObjectMapper(); // 모든 필드에 대해 JSON 변환 허용 + 타입 정보 보존
		
		//모든 필드 대상으로 직렬화 허용
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.activateDefaultTyping(
				LaissezFaireSubTypeValidator.instance,
				ObjectMapper.DefaultTyping.NON_FINAL
		);
		// → 역직렬화할 때 타입 정보를 보존하도록 설정
		
		 // 5. 빈 객체를 직렬화할 때 에러 발생 방지
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		// 6. 설정한 ObjectMapper를 직렬화기에 등록
		serializer.setObjectMapper(mapper);
		return serializer;
	}

}
