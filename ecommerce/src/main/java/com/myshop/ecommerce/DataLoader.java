package com.myshop.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.myshop.ecommerce.entity.Product;
import com.myshop.ecommerce.repository.ProductRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository repository;

    public DataLoader(ProductRepository repository) {
        this.repository = repository;
    }

     @Override
  public void run(String... args) throws Exception {
    if (repository.count() == 0) {  
      // Women's Collection
      repository.save(new Product(120.00, "women", "blue", "flowers", "dress", "/images/products/product1.jpg"));
      repository.save(new Product(109.99, "women", "pink", "flowers", "dress", "/images/products/product2.jpg"));
      repository.save(new Product(59.99, "women", "pink", "leather", "bag", "/images/products/product3.jpg"));
      repository.save(new Product(79.90, "women", "pink", "lace", "dress", "/images/products/product4.jpg"));
      repository.save(new Product(139.90, "women", "pink", "lace", "dress", "/images/products/product5.jpg"));
      repository.save(new Product(80.00, "women", "pink", "fluffy", "jacket", "/images/products/product6.jpg"));
      repository.save(new Product(99.99, "women", "pink", "leather", "bag", "/images/products/product7.jpg"));
      repository.save(new Product(89.99, "women", "white", "leather", "bag", "/images/products/product8.jpg"));
      repository.save(new Product(59.99, "women", "bluejean", "jean", "skirt", "/images/products/product9.jpg"));
      repository.save(new Product(88.00, "women", "blue", "fluffy", "skirt", "/images/products/product10.jpg"));
      repository.save(new Product(74.90, "women", "white", "leather", "heel", "/images/products/product11.jpg"));
      repository.save(new Product(164.90, "women", "pink", "flowers", "dress", "/images/products/product12.jpg"));
      repository.save(new Product(54.99, "women", "black", "Jean", "skirt", "/images/products/product13.jpg"));
      repository.save(new Product(64.90, "women", "blue", "flowers", "shirt", "/images/products/product14.jpg"));
      repository.save(new Product(89.90, "women", "white", "flowers", "shirt", "/images/products/product15.jpg"));
      repository.save(new Product(79.90, "women", "white", "flowers", "dress", "/images/products/product16.jpg"));
      repository.save(new Product(100.90, "women", "bluejean", "jean", "pants", "/images/products/product17.jpg"));
      repository.save(new Product(89.90, "women", "bluejean", "jean", "skirt", "/images/products/product18.jpg"));
      repository.save(new Product(64.90, "women", "bluejean", "jean", "pants", "/images/products/product19.jpg"));
      repository.save(new Product(80.00, "women", "green", "leather", "heel", "/images/products/product20.jpg"));

      // Men's Collection
      repository.save(new Product(49.90, "men", "black", "cotton", "tshirt", "/images/products/product21.jpg"));
      repository.save(new Product(39.90, "men", "green", "cotton", "tshirt", "/images/products/product22.jpg"));
      repository.save(new Product(200.90, "men", "black", "Laine", "jacket", "/images/products/product23.jpg"));
      repository.save(new Product(88.90, "men", "black", "Laine", "pants", "/images/products/product24.jpg"));
      repository.save(new Product(79.99, "men", "white", "cotton", "shirt", "/images/products/product25.jpg"));
      repository.save(new Product(140.90, "men", "green", "Laine", "jacket", "/images/products/product26.jpg"));
      repository.save(new Product(104.90, "men", "green", "Laine", "pants", "/images/products/product27.jpg"));
      repository.save(new Product(89.90, "men", "brown", "Leather", "shoes", "/images/products/product28.jpg"));
      repository.save(new Product(100.90, "men", "blue", "suede", "shoes", "/images/products/product29.jpg"));
      repository.save(new Product(110.90, "men", "brown", "suede", "shoes", "/images/products/product30.jpg"));
  }}
}