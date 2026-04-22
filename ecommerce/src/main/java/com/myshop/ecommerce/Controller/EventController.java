package com.myshop.ecommerce.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.service.EventService;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/event")
    public String saveEvent(@RequestBody EventRequest request) {
        service.saveEvent(
            request.getUserId(),
            request.getProductId(),
            request.getAction()
        );
        return "Event saved!";
    }
    @CrossOrigin(origins = "*") 
    @GetMapping("/liked/{userId}")
    public List<Product> getLikedProducts(@PathVariable Long userId) {
        return service.getLikedProductsForUser(userId);
    }

    public static class EventRequest {
        private Long userId;
        private Long productId;
        private String action;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }
}

