package com.myshop.ecommerce.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.myshop.ecommerce.dto.RecommendationDTO;
import com.myshop.ecommerce.service.RecommendationService;

@Service
public class KafkaConsumerService {

    private final RecommendationService recommendationService;

    public KafkaConsumerService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @KafkaListener(
        topics = "recommended-products",
        groupId = "recommendation-engine-group"
    )
    public void consumeRecommendation(RecommendationDTO dto) {
        System.out.println("📥 Received recommendations for user " 
            + dto.getUserId() + ": " + dto.getRecommendedProductIds());
        
        recommendationService.saveRecommendations(dto);
    }
}