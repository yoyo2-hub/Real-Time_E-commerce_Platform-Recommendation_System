package com.myshop.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getProductsByType(String type) {
        return repository.findByType(type);
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElse(null);
    }
}