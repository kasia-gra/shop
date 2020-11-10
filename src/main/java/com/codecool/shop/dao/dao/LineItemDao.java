package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.product.Product;

public interface LineItemDao {

    void add(LineItem lineItem);
    LineItem find(int id);
    void update(int cartId, Product product);
    void remove(int id);
    void addOneItem(int cartId, Product product);
    void updateQty(int cartId, Product product, int qty);
}
