package com.example.reco_engine;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EventRepository 
    extends MongoRepository<UserEvent, String> {
    
    List<UserEvent> findByUserId(String userId);
    List<UserEvent> findByProductId(String productId);
    List<UserEvent> findByEventType(String eventType);
}