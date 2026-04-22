package com.myshop.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myshop.ecommerce.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{
    // Find all events for a specific user and type (e.g., "like")
    List<Event> findByUserIdAndEventType(Long userId, String eventType);
}