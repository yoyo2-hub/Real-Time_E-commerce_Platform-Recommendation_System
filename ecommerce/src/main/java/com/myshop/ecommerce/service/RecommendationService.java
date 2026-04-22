package com.myshop.ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.myshop.ecommerce.dto.RecommendationDTO;
import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.entity.Recommendation;
import com.myshop.ecommerce.repository.ProductRepository;
import com.myshop.ecommerce.repository.RecommendationRepository;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ProductRepository productRepository;

    public RecommendationService(RecommendationRepository recommendationRepository,
                                  ProductRepository productRepository) {
        this.recommendationRepository = recommendationRepository;
        this.productRepository = productRepository;
    }

    public void saveRecommendations(RecommendationDTO dto) {
        Recommendation rec = new Recommendation();
        rec.setUserId(dto.getUserId());
        rec.setRecommendedProductIds(dto.getRecommendedProductIds());
        // updatedAt is set automatically by @PrePersist
        recommendationRepository.save(rec);
        System.out.println("✅ Saved recommendations to MySQL for user " + dto.getUserId());
    }

    public List<Product> getRecommendedProductsForUser(Long userId) {
        return recommendationRepository
            .findTopByUserIdOrderByIdDesc(userId)
            .map(rec -> {
                List<Long> orderedIds = rec.getRecommendedProductIds();
                List<Product> products = productRepository.findAllById(orderedIds);

                Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, p -> p));

                return orderedIds.stream()
                    .map(productMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            })
            .orElse(List.of());
    }

    public Map<String, Object> getLatestRecommendationTimestamp(Long userId) {
        Map<String, Object> result = new HashMap<>();
        recommendationRepository.findTopByUserIdOrderByIdDesc(userId)
            .ifPresentOrElse(
                rec -> {
                    result.put("id", rec.getId());
                    result.put("updatedAt", rec.getUpdatedAt().toString());
                },
                () -> {
                    result.put("id", null);
                    result.put("updatedAt", null);
                }
            );
        return result;
    }
}