package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.product.Product;

public interface CartDao {
    void add(Cart cart, int productId);
    Cart find(int id);
    void addItemToCart(int cartId, int productId, int addedQuantity);
    void remove(int id);
}
