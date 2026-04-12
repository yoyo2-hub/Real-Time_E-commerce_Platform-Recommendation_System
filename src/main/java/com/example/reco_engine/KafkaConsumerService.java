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
    private final ProductRepository productRepo;

    public KafkaConsumerService(EventRepository eventRepo,
                                RecommendationService recoService,
                                ProductRepository productRepo) {
        this.eventRepo = eventRepo;
        this.recoService = recoService;
        this.productRepo = productRepo;
    }

    @KafkaListener(topics = "user-interactions", groupId = "reco-group")
    public void consume(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            // Read full DTO — contains both event AND product
            UserInteractionDTO dto = mapper.readValue(message, UserInteractionDTO.class);
            UserEvent event = dto.getEvent();
            Product incoming = dto.getProduct();

            if (event == null || incoming == null) {
                System.err.println("❌ DTO incomplet — event ou product est null");
                return;
            }

            // Set timestamp if missing
            if (event.getTimestamp() == null) {
                event.setTimestamp(LocalDateTime.now());
            }

            System.out.println("📩 Event reçu :"
                + " userId=" + event.getUserId()
                + " | productId=" + event.getProductId()
                + " | eventType=" + event.getEventType()
                + " | category=" + incoming.getCategory()
                + " | color=" + incoming.getColor());

            // 1. Save the event to MongoDB
            eventRepo.save(event);

            // 2. Save product to MongoDB if not already there
            String productIdStr = String.valueOf(event.getProductId());
            if (!productRepo.existsById(productIdStr)) {
                Product p = new Product();
                p.setId(productIdStr);
                p.setCategory(incoming.getCategory());
                p.setColor(incoming.getColor());
                p.setMotif(incoming.getMotif());
                p.setType(incoming.getType());
                p.setPrice(incoming.getPrice());
                p.setImage(incoming.getImage());
                productRepo.save(p);
                System.out.println("✅ Nouveau produit sauvegardé dans MongoDB : id=" + productIdStr
                    + " | category=" + incoming.getCategory());
            } else {
                System.out.println("ℹ️ Produit déjà en MongoDB : id=" + productIdStr);
            }

            // 3. Run recommendation engine
            if (event.getEventType().equals("click") ||
                event.getEventType().equals("view")) {
                recoService.updateRecommendations(
                    event.getUserId(),
                    productIdStr,
                    event.getEventType()
                );
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur parsing event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}