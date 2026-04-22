package com.myshop.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myshop.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByType(String type);
    // Finds products with the same category, but excludes the current product ID
    List<Product> findByCategoryAndIdNot(String category, Long id);
}
