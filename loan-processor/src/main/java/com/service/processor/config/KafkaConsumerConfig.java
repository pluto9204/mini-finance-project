package com.service.processor.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.service.loan.apply.dto.RequestLoanDTO;

@Configuration
public class KafkaConsumerConfig {
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		
		factory.setConsumerFactory(consumerFactory()); //KafkaListener가 사용할 컨슈머 팩토리를 등록함.
		return factory;
	}
	
	//Kafka 컨슈머의 동작 방식을 정의
	public ConsumerFactory<String, Object> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");	//Kafka 브로커 주소
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "loan-processor-group");	//컨슈머 그룹 ID 설정
		
		JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
		deserializer.setTypeMapper(typeMapper());	//__TypeId__ 헤더를 해석해서 어떤 클래스로 역직렬화할지 결정하는 매퍼 설정
		deserializer.setUseTypeMapperForKey(false);	//메시지의 Key는 String으로 처리하고, 매퍼는 사용하지 않겠다는 의미
		deserializer.addTrustedPackages("com.service.loan.*");	//보안 설정: 해당 패키지에 있는 클래스만 역직렬화 허용
		
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);	//설정한 프로퍼티와 deserializer로 Kafka Consumer Factory 생성
	}

	private DefaultJackson2JavaTypeMapper typeMapper() {
		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
		Map<String, Class<?>> idClassMapping = new HashMap<>();
		idClassMapping.put("RequestLoanDTO", RequestLoanDTO.class);	//이 메서드에서 __TypeId__ 헤더값이 어떤 클래스를 의미하는지 등록해줘
		
		typeMapper.setIdClassMapping(idClassMapping);
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID); // 우선순위 설정, 메시지를 역직렬화할 때, JSON 내부의 구조보다는 헤더의 __TypeId__ 값을 우선해서 어떤 클래스로 변환할지 판단하도록 설정
        return typeMapper;
	}
}
