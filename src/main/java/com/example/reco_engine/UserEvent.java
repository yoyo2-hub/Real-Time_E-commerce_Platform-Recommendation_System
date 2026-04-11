package com.example.reco_engine;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "user_events")
public class UserEvent {

    @Id
    private String id;
    private String userId;
    private String productId;
    private String eventType;    // "view" ou "click"
    private LocalDateTime timestamp;

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getProductId() { return productId; }
    public String getEventType() { return eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}