package com.example.reco_engine;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recommendations")
public class Recommendation {   // ← R majuscule, convention Java

    @Id
    private String productId;

    private List<String> recommendedProducts;

    // Getters
    public String getProductId() { return productId; }
    public List<String> getRecommendedProducts() { return recommendedProducts; }

    // Setters
    public void setProductId(String productId) { this.productId = productId; }
    public void setRecommendedProducts(List<String> recommendedProducts) {
        this.recommendedProducts = recommendedProducts;
    }
}