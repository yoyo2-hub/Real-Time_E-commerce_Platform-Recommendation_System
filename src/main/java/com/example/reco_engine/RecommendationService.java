package com.example.reco_engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private RecommendationRepository recoRepo;

    public void updateRecommendations(String productId, String eventType) {

        // 1. Récupérer le produit consulté
        Optional<Product> optProduct = productRepo.findById(productId);
        if (optProduct.isEmpty()) {
            System.out.println("⚠️ Produit non trouvé : " + productId);
            return;
        }

        Product product = optProduct.get();
        Map<String, Integer> scoreMap = new HashMap<>();

        // 2. Logique selon le type d'événement
        if (eventType.equals("view")) {
            // Vue → recommandation basée sur catégorie + couleur + motif
            System.out.println("👁️ View détecté → recommandation par attributs");

            // Même catégorie → score +3
            productRepo.findByCategory(product.getCategory())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 3, Integer::sum);
                });

            // Même couleur → score +2
            productRepo.findByColor(product.getColor())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 2, Integer::sum);
                });

            // Même motif → score +2
            productRepo.findByMotif(product.getMotif())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 2, Integer::sum);
                });

            // Même type → score +1
            productRepo.findByType(product.getType())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 1, Integer::sum);
                });

        } else if (eventType.equals("click")) {
            // Click → recommandation plus forte sur catégorie
            System.out.println("🖱️ Click détecté → recommandation renforcée");

            // Même catégorie → score +5 (plus fort que view)
            productRepo.findByCategory(product.getCategory())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 5, Integer::sum);
                });

            // Même couleur → score +3
            productRepo.findByColor(product.getColor())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 3, Integer::sum);
                });

            // Même motif → score +3
            productRepo.findByMotif(product.getMotif())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 3, Integer::sum);
                });

            // Même type → score +2
            productRepo.findByType(product.getType())
                .forEach(p -> {
                    if (!p.getId().equals(productId))
                        scoreMap.merge(p.getId(), 2, Integer::sum);
                });
        }

        // 3. Trier par score et prendre Top 5
        List<String> top5 = scoreMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // 4. Sauvegarder dans MongoDB
        Recommendation reco = recoRepo.findById(productId)
            .orElse(new Recommendation());
        reco.setProductId(productId);
        reco.setRecommendedProducts(top5);
        recoRepo.save(reco);

        System.out.println("✅ Top 5 recommandations pour [" + productId + "] : " + top5);
    }

    public List<String> getRecommendations(String productId) {
        return recoRepo.findById(productId)
            .map(Recommendation::getRecommendedProducts)
            .orElse(Collections.emptyList());
    }
}