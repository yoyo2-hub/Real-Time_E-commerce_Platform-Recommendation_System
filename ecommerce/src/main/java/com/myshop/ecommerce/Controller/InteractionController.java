package com.myshop.ecommerce.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.ecommerce.dto.UserInteractionDTO;
import com.myshop.ecommerce.entity.Event;
import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.kafka.KafkaProducerService;
import com.myshop.ecommerce.repository.EventRepository;

@RestController
@RequestMapping("/api/interactions")
@CrossOrigin(origins = "*")
public class InteractionController {

    private final KafkaProducerService kafkaProducerService;
    private final EventRepository eventRepository;

    public InteractionController(KafkaProducerService kafkaProducerService, EventRepository eventRepository) {
        this.kafkaProducerService = kafkaProducerService;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/{productId}/click")
    public ResponseEntity<String> handleProductClick(
            @PathVariable Long productId,
            @RequestBody Product product) {

        Long currentUserId = 1L; // Synchronized with my JS userId
        Event clickEvent = new Event(currentUserId, productId, "click");

        eventRepository.save(clickEvent);

        UserInteractionDTO dto = new UserInteractionDTO(clickEvent, product);
        kafkaProducerService.sendInteraction(dto);

        return ResponseEntity.ok("Click tracked and sent to Recommendation Engine!");
    }
}