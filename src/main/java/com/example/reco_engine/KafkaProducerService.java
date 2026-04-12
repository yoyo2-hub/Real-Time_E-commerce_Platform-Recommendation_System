package com.example.reco_engine;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, RecommendationDTO> kafkaTemplate;
    private static final String TOPIC = "recommended-products";

    public KafkaProducerService(KafkaTemplate<String, RecommendationDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRecommendations(String userId, List<String> productIds) {
        try {
            Long userIdLong = Long.parseLong(userId);
            List<Long> productIdsLong = productIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

            RecommendationDTO dto = new RecommendationDTO(userIdLong, productIdsLong);
            kafkaTemplate.send(TOPIC, dto);
            System.out.println("📤 Sent → userId=" + userId + " | produits=" + productIds);

        } catch (NumberFormatException e) {
            System.err.println("❌ Erreur conversion IDs : " + e.getMessage());
        }
    }
}