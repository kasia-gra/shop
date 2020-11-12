package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.Order;

import java.util.List;

public interface OrderDao {
    void add(Order order, int productId);
    Order find(int id);
    void update(Order order);
    void remove(Order order);
    void removeItem(int productId, Cart cart);
    void addItemToOrder(Order order, int productId, int addedQuantity);
    List<Order> getAll();
    List<Order> getBy(int userId);
    Order getActual(int sessionId);
}
