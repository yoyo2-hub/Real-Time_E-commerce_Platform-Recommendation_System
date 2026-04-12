package com.example.reco_engine;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "recommended-products";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRecommendations(String userId, java.util.List<String> productIds) {
        String json = "{\"userId\":\"" + userId + "\","
                    + "\"recommendedProductIds\":" + productIds + "}";
        kafkaTemplate.send(TOPIC, json);
        System.out.println("📤 Recommandations envoyées → " + json);
    }
}