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

    @Autowired
    private KafkaProducerService kafkaProducer;

    // ← userId ajouté
    public void updateRecommendations(String userId, String productId, String eventType) {

        Optional<Product> optProduct = productRepo.findById(productId);
        if (optProduct.isEmpty()) {
            System.out.println("⚠️ Produit non trouvé : " + productId);
            return;
        }

        Product product = optProduct.get();
        Map<String, Integer> scoreMap = new HashMap<>();

        if (eventType.equals("view")) {
            System.out.println("👁️ View détecté → recommandation par attributs");
            productRepo.findByCategory(product.getCategory())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 3, Integer::sum); });
            productRepo.findByColor(product.getColor())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 2, Integer::sum); });
            productRepo.findByMotif(product.getMotif())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 2, Integer::sum); });
            productRepo.findByType(product.getType())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 1, Integer::sum); });

        } else if (eventType.equals("click")) {
            System.out.println("🖱️ Click détecté → recommandation renforcée");
            productRepo.findByCategory(product.getCategory())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 5, Integer::sum); });
            productRepo.findByColor(product.getColor())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 3, Integer::sum); });
            productRepo.findByMotif(product.getMotif())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 3, Integer::sum); });
            productRepo.findByType(product.getType())
                .forEach(p -> { if (!p.getId().equals(productId)) scoreMap.merge(p.getId(), 2, Integer::sum); });
        }

        List<String> top5 = scoreMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        Recommendation reco = recoRepo.findById(productId)
            .orElse(new Recommendation());
        reco.setProductId(productId);
        reco.setRecommendedProducts(top5);
        recoRepo.save(reco);

        System.out.println("✅ Top 5 pour [" + productId + "] : " + top5);

        // ← NOUVEAU : envoie vers Kafka
        kafkaProducer.sendRecommendations(userId, top5);
    }

    public List<String> getRecommendations(String productId) {
        return recoRepo.findById(productId)
            .map(Recommendation::getRecommendedProducts)
            .orElse(Collections.emptyList());
    }
}