package com.codecool.shop.cart;

import com.codecool.shop.model.Product;

public class Order {
    private int id;
    private final Product lineItem;
    private int quantity;
    private float totalPrice;

    public Order(Product lineItem) {
        this.lineItem = lineItem;
        this.quantity = 1;
        this.totalPrice = lineItem.getDefaultPrice() * quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = lineItem.getDefaultPrice() * quantity;
    }

    public Product getLineItem() {
        return lineItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }
}
