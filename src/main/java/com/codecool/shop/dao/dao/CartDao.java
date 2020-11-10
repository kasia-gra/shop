package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;

public interface CartDao {
    void addEmptyCart(Cart cart);
    Cart find(int id);
    void addItemToCart(int cartId, int productId, int addedQuantity);
    void removeItemFromCart(int lineItemId, int cartId);
    void remove(int id);
}
