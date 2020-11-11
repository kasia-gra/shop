package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.LineItem;

import java.util.List;

public interface LineItemDao {

    void addProduct(int cartId, int productId, int quantity);
    LineItem find(int id);
    void remove(int id);
    List<LineItem> findLineItemsByCartId(int cartId);
    int getTotalValueOfLinesInCart(int cartId);
    int getTotalNumberOfLinesInCart(int cartId);
}
