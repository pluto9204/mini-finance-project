package com.service.loan.util;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaUtil {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // 단순 발행 (비동기 콜백 포함)
    public void sendMessage(String topic, Object payload) {
    	Message<Object> message = MessageBuilder.withPayload(payload)
    			.setHeader(KafkaHeaders.TOPIC, topic)
    			.setHeader("__TypeId__", "loan-apply")
    			.build();
    	
        kafkaTemplate.send(message)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("[Kafka 전송 성공] topic: " + topic + ", offset: " + result.getRecordMetadata().offset());
                } else {
                    System.out.println("[Kafka 전송 실패] topic: " + topic + ", error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
    }

    // key 포함 발행 (비동기 콜백 포함)
    public void sendMessageWithKey(String topic, String key, Object payload) {
    	Message<Object> message = MessageBuilder.withPayload(payload)
    			.setHeader(KafkaHeaders.TOPIC, topic)
    			.setHeader(KafkaHeaders.KEY, key)
    			.setHeader("__TypeId__", "loan-apply")
    			.build();
    	
        kafkaTemplate.send(message)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("[Kafka 전송 성공] topic: " + topic + ", key: " + key + ", offset: " + result.getRecordMetadata().offset());
                } else {
                    System.out.println("[Kafka 전송 실패] topic: " + topic + ", key: " + key + ", error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
    }

    // 발행 후 결과 리턴
    public CompletableFuture<SendResult<String, Object>> sendMessageWithResult(String topic, Object message) {
        return kafkaTemplate.send(topic, message);
    }
}
