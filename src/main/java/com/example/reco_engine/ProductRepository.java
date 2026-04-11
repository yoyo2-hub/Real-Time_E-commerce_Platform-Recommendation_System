package com.example.reco_engine;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository 
    extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);
    List<Product> findByColor(String color);
    List<Product> findByMotif(String motif);
    List<Product> findByType(String type);
}