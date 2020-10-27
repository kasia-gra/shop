package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.User;

public class Order {
    private User user;
    private Cart cart;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
