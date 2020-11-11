package com.codecool.shop.model.order;

import com.codecool.shop.model.AddressDetail;
import com.codecool.shop.model.Session;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.user.User;

import java.util.Date;

public class Order {
    private int id;
    private User user;
    private Cart cart;
    private Payment payment;
    private AddressDetail orderAddressDetail;
    private Session session;
    private Status status;
    private Date date;

    public Order() {
        this.cart = new Cart();
    }

    public Order(Cart cart, Session session) {
        this.cart = cart;
        this.session = session;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public AddressDetail getOrderAddressDetail() {
        return orderAddressDetail;
    }

    public void setOrderAddressDetail(AddressDetail orderAddressDetail) {
        this.orderAddressDetail = orderAddressDetail;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
