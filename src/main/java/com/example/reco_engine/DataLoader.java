package com.example.reco_engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepo;

    @Override
    public void run(String... args) {

        if (productRepo.count() == 0) {
            // Robes
            save("P001","Robe Fleurie Bleue","dress",49.99,"women","blue","floral","https://source.unsplash.com/600x600/?dress");
            save("P002","Robe Rouge Rayée","dress",59.99,"women","red","striped","https://source.unsplash.com/600x600/?dress");
            save("P003","Robe Bleue Unie","dress",39.99,"women","blue","plain","https://source.unsplash.com/600x600/?dress");
            save("P004","Robe Verte Fleurie","dress",55.99,"women","green","floral","https://source.unsplash.com/600x600/?dress");
            save("P005","Robe Noire Unie","dress",65.99,"women","black","plain","https://source.unsplash.com/600x600/?dress");

            // Chemises
            save("P006","Chemise Bleue Fleurie","shirt",29.99,"women","blue","floral","https://source.unsplash.com/600x600/?shirt");
            save("P007","Chemise Rouge Unie","shirt",25.99,"men","red","plain","https://source.unsplash.com/600x600/?shirt");
            save("P008","Chemise Noire Rayée","shirt",35.99,"women","black","striped","https://source.unsplash.com/600x600/?shirt");

            // Pantalons
            save("P009","Pantalon Noir Uni","pants",45.99,"women","black","plain","https://source.unsplash.com/600x600/?pants");
            save("P010","Pantalon Bleu Rayé","pants",42.99,"men","blue","striped","https://source.unsplash.com/600x600/?pants");

            // Jupes
            save("P011","Jupe Bleue Fleurie","skirt",32.99,"women","blue","floral","https://source.unsplash.com/600x600/?skirt");
            save("P012","Jupe Rouge Unie","skirt",28.99,"women","red","plain","https://source.unsplash.com/600x600/?skirt");

            System.out.println("✅ " + productRepo.count() + " produits chargés !");
        } else {
            System.out.println("✅ Produits déjà en base : " + productRepo.count());
        }
    }

    // ← Méthode corrigée avec imageUrl en paramètre
    private void save(String id, String name, String category,
                      double price, String type, String color, 
                      String motif, String imageUrl) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setType(type);
        p.setColor(color);
        p.setMotif(motif);
        p.setImage(imageUrl);  // ← NOUVEAU
        productRepo.save(p);
    }
}