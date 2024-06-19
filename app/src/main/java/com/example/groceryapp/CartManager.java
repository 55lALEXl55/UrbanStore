package com.example.groceryapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartManager {

    private static CartManager instance;
    private Map<Product, Integer> cartItems;

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProductToCart(Product product) {
        if (cartItems.containsKey(product)) {
            cartItems.put(product, cartItems.get(product) + 1);
        } else {
            cartItems.put(product, 1);
        }
    }

    public void removeProductFromCart(Product product) {
        if (cartItems.containsKey(product)) {
            int quantity = cartItems.get(product);
            if (quantity > 1) {
                cartItems.put(product, quantity - 1);
            } else {
                cartItems.remove(product);
            }
        }
    }

    public ArrayList<Product> getCartItems() {
        return new ArrayList<>(cartItems.keySet());
    }

    public int getProductQuantity(Product product) {
        return cartItems.getOrDefault(product, 0);
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
