package com.example.reco_engine;

public class UserInteractionDTO {
    private UserEvent event;
    private Product product;

    public UserInteractionDTO() {}

    public UserEvent getEvent() { return event; }
    public void setEvent(UserEvent event) { this.event = event; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}