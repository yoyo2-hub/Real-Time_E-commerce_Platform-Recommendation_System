package com.example.reco_engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class KafkaConsumerService {

    private final EventRepository eventRepo;
    private final RecommendationService recoService;

    public KafkaConsumerService(EventRepository eventRepo,
                                RecommendationService recoService) {
        this.eventRepo = eventRepo;
        this.recoService = recoService;
    }

    @KafkaListener(topics = "user-interactions", groupId = "reco-group")
    public void consume(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            UserEvent event = mapper.readValue(message, UserEvent.class);

            // Ajouter timestamp automatiquement si absent
            if (event.getTimestamp() == null) {
                event.setTimestamp(LocalDateTime.now());
            }

            System.out.println("📩 Event reçu :"
                + " userId=" + event.getUserId()
                + " | productId=" + event.getProductId()
                + " | eventType=" + event.getEventType()
                + " | timestamp=" + event.getTimestamp());

            // 1. Stocker l'événement
            eventRepo.save(event);

            // 2. Mettre à jour recommandations selon eventType
            if (event.getEventType().equals("view") || 
                event.getEventType().equals("click")) {
                recoService.updateRecommendations(
                    event.getProductId(), 
                    event.getEventType()
                );
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur parsing event: " + e.getMessage());
        }
    }
}