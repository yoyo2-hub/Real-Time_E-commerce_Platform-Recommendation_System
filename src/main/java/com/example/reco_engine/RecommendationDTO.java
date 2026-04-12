package com.example.reco_engine;

import java.util.List;

public class RecommendationDTO {
    private Long userId;
    private List<Long> recommendedProductIds;

    public RecommendationDTO() {}

    public RecommendationDTO(Long userId, List<Long> recommendedProductIds) {
        this.userId = userId;
        this.recommendedProductIds = recommendedProductIds;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<Long> getRecommendedProductIds() { return recommendedProductIds; }
    public void setRecommendedProductIds(List<Long> ids) { this.recommendedProductIds = ids; }
}