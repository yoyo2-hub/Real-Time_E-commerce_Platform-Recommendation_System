package com.example.reco_engine;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String category;   // dress, shirt, pants, skirt...
    private double price;
    private String type;       // men, women
    private String color;      // blue, red, black...
    private String motif;      // floral, striped, plain...
    private String image;      // URL de l image du produit ...
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getType() { return type; }
    public String getColor() { return color; }
    public String getMotif() { return motif; }
    public String getImage() { return image; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setType(String type) { this.type = type; }
    public void setColor(String color) { this.color = color; }
    public void setMotif(String motif) { this.motif = motif; }
    public void setImage(String image) {this.image= image;}
}