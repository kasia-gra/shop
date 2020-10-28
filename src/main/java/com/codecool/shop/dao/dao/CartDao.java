package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;

public interface CartDao {
    int add(Cart cart);
    Cart find(int id);
    void update(Cart cart);
    void remove(int id);
}
