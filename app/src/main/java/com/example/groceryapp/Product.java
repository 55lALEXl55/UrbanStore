package com.example.groceryapp;

public class Product {

    private String title;
    private String category;
    private String image;
    private double price;
    private int quantity;

    // Constructor sin argumentos necesario para Firebase
    public Product() {
    }

    public Product(String title, String category, String image, double price, int quantity) {
        this.title = title;
        this.category = category;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters y setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
