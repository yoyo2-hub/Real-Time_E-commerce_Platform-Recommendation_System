package com.example.reco_engine;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    // Recommendation avec R majuscule !
}