package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;

import java.util.List;

public interface LineItemDao {

    void addProduct(Cart cart, int productId, int quantity);
    LineItem find(int product_id, int cartId);
    void remove(int productId, Cart cart);
    List<LineItem> findLineItemsByCartId(int cartId);
    int getTotalValueOfLinesInCart(int cartId);
    int getTotalNumberOfLinesInCart(int cartId);
}
