package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;

public interface CartDao {
    void add(Cart cart);
    Cart find(int id);
    void addItemToCart(Cart cart, int productPrice, int addedQuantity);
    void remove(int id);
}
