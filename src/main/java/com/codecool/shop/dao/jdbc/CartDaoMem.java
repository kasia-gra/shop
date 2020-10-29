package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.CartDao;
import com.codecool.shop.model.order.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDaoMem implements CartDao {
    private int sequenceNumber = 1;
    private List<Cart> data = new ArrayList<>();
    private static CartDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private CartDaoMem() {
    }

    public static CartDaoMem getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public int add(Cart cart) {
        cart.setId(sequenceNumber);
        data.add(cart);
        sequenceNumber++;
        return cart.getId();
    }

    @Override
    public Cart find(int id) {
        return data.stream().
                filter(cart -> cart.getId() == id).
                findFirst().
                orElse(null);
    }

    @Override
    public void update(Cart newCart) {
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }
}
