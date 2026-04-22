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

    public void updateRecommendations(String userId, String productId, String eventType) {

        // 1. Find the clicked product in MongoDB
        Optional<Product> optProduct = productRepo.findById(productId);
        if (optProduct.isEmpty()) {
            System.out.println("⚠️ Produit non trouvé dans MongoDB : " + productId);
            return;
        }

        Product product = optProduct.get();
        Map<String, Integer> scoreMap = new HashMap<>();

        // 2. Score similar products based on event type
        if (eventType.equals("view")) {
            System.out.println("👁️ View détecté → recommandation par attributs");

            productRepo.findByCategory(product.getCategory())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 3, Integer::sum); });

            productRepo.findByColor(product.getColor())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 2, Integer::sum); });

            productRepo.findByMotif(product.getMotif())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 2, Integer::sum); });

            productRepo.findByType(product.getType())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 1, Integer::sum); });

        } else if (eventType.equals("click")) {
            System.out.println("🖱️ Click détecté → recommandation renforcée");

            productRepo.findByCategory(product.getCategory())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 5, Integer::sum); });

            productRepo.findByColor(product.getColor())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 3, Integer::sum); });

            productRepo.findByMotif(product.getMotif())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 3, Integer::sum); });

            productRepo.findByType(product.getType())
                .forEach(p -> { if (!p.getId().equals(productId))
                    scoreMap.merge(p.getId(), 2, Integer::sum); });
        }

        // 3. Not enough products in MongoDB yet to make recommendations
        if (scoreMap.isEmpty()) {
            System.out.println("ℹ️ Pas assez de produits en MongoDB pour recommander."
                + " L'utilisateur doit cliquer sur plus de produits d'abord.");
            return;
        }

        // 4. Sort by score and take Top 5
        List<String> top5 = scoreMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // 5. Save to MongoDB
        Recommendation reco = recoRepo.findById(productId)
            .orElse(new Recommendation());
        reco.setProductId(productId);
        reco.setRecommendedProducts(top5);
        recoRepo.save(reco);

        System.out.println("✅ Top 5 pour produit [" + productId + "] : " + top5);

        // 6. Send back to Service A via Kafka
        kafkaProducer.sendRecommendations(userId, top5);
    }

    public List<String> getRecommendations(String productId) {
        return recoRepo.findById(productId)
            .map(Recommendation::getRecommendedProducts)
            .orElse(Collections.emptyList());
    }
}