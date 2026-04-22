package com.myshop.ecommerce.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.myshop.ecommerce.dto.UserInteractionDTO;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, UserInteractionDTO> kafkaTemplate;

    // The topic name Service B is listening to
    private static final String TOPIC = "user-interactions";
    public KafkaProducerService(KafkaTemplate<String, UserInteractionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInteraction(UserInteractionDTO dto) {
        kafkaTemplate.send(TOPIC, dto);
        System.out.println("✅ Sent to Kafka topic '" + TOPIC + "': " + dto);
    }
}