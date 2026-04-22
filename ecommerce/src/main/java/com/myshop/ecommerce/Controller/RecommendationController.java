package com.myshop.ecommerce.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public List<Product> getRecommendations(@PathVariable Long userId) {
        return recommendationService.getRecommendedProductsForUser(userId);
    }

    
    @GetMapping("/{userId}/latest-timestamp")
    public Map<String, Object> getLatestTimestamp(@PathVariable Long userId) {
          return recommendationService.getLatestRecommendationTimestamp(userId);
      }
}