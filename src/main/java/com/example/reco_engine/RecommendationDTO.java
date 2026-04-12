package com.example.reco_engine;

import java.util.List;

public class RecommendationDTO {
    private String userId;
    private List<String> recommendedProductIds;

    public RecommendationDTO() {}

    public RecommendationDTO(String userId, List<String> recommendedProductIds) {
        this.userId = userId;
        this.recommendedProductIds = recommendedProductIds;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getRecommendedProductIds() { return recommendedProductIds; }
    public void setRecommendedProductIds(List<String> ids) { this.recommendedProductIds = ids; }
}