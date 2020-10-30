package com.codecool.shop.model.order;

import com.codecool.shop.model.user.User;

public class Order {
    private int id;
    private User user;
    private Cart cart;
    private boolean isArchival = false;
    private Payment payment;

    public Order(User user) {
        this.user = user;
        this.cart = new Cart();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public boolean isArchival() {
        return isArchival;
    }

    public void setArchival(boolean isArchival) {
        this.isArchival = isArchival;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
