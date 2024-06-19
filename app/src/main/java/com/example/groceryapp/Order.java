package com.example.groceryapp;

import java.util.List;

public class Order {

    private String orderId;
    private List<Product> productList;
    private double totalAmount;
    private String orderDate;

    // Constructor vacío necesario para Firebase
    public Order() {
    }

    // Constructor con parámetros
    public Order(String orderId, List<Product> productList, double totalAmount, String orderDate) {
        this.orderId = orderId;
        this.productList = productList;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters y setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
