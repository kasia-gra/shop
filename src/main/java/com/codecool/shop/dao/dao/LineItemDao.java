package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.product.Product;

import java.util.List;

public interface LineItemDao {

    void add(int cartId, int productId);
    LineItem find(int id);
    void update(int cartId, Product product);
    void remove(int id);
    void addOneItem(int cartId, Product product);
    void updateQty(int cartId, int productId, int qty);
    List<LineItem> findLineItemsByCartId(int cartId);
    int getTotalValueOfLinesInCart(int cartId);
    int getTotalNumberOfLinesInCart(int cartId);
}
