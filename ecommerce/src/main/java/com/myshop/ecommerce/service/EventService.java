package com.myshop.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.ecommerce.entity.Event;
import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.repository.EventRepository;
import com.myshop.ecommerce.repository.ProductRepository;

@Service
public class EventService {
    private final EventRepository repository;
    private final ProductRepository productRepository; // Add this

    public EventService(EventRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public void saveEvent(Long userId, Long productId, String eventType) {
        Event event = new Event(userId, productId, eventType);
        repository.save(event);
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    // ADD THIS NEW METHOD
    public List<Product> getLikedProductsForUser(Long userId) {
        // 1. Get all "like" events for this user
        List<Event> likes = repository.findByUserIdAndEventType(userId, "like");
        
        // 2. Extract just the Product IDs
        List<Long> productIds = likes.stream()
                                     .map(Event::getProductId)
                                     .toList();
                                     
        // 3. Fetch and return those specific products from the database
        return productRepository.findAllById(productIds);
    }
}