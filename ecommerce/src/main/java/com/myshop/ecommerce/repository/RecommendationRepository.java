package com.myshop.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myshop.ecommerce.entity.Recommendation;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    // Find the latest recommendations for a specific user
    Optional<Recommendation> findTopByUserIdOrderByIdDesc(Long userId);
}