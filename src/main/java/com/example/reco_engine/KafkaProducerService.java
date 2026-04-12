package com.example.reco_engine;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, RecommendationDTO> kafkaTemplate;
    private static final String TOPIC = "recommended-products";

    public KafkaProducerService(KafkaTemplate<String, RecommendationDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRecommendations(String userId, List<String> productIds) {
        RecommendationDTO dto = new RecommendationDTO(userId, productIds);
        kafkaTemplate.send(TOPIC, dto);
        System.out.println("📤 Recommandations envoyées → userId=" + userId + " | produits=" + productIds);
    }
}