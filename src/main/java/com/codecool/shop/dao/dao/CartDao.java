package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;

public interface CartDao {
    void addEmptyCart(Cart cart, int productId);
    Cart find(int id);
    void addItemToCart(Cart cart, int productId, int addedQuantity);
    void removeItemFromCart(int lineItemId, int cartId);
    void remove(int id);
}
