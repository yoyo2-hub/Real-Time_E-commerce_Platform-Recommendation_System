package com.example.reco_engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recoService;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private RecommendationRepository recoRepo;

    // 1. Obtenir les recommandations pour un produit
    @GetMapping("/recommendations")
    public List<Product> getRecos(@RequestParam String productId) {
        List<String> ids = recoService.getRecommendations(productId);
        return productRepo.findAllById(ids);
    }

    // 2. Récupérer tous les produits
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // 3. Récupérer un produit par ID
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable String id) {
        return productRepo.findById(id).orElse(null);
    }
}