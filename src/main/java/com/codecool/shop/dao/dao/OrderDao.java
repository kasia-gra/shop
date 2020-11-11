package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Order;

import java.util.List;

public interface OrderDao {
    void add(Order order, int productId);
    Order find(int id);
    void update(Order order);
    void remove(int id);
    void addItemToOrder(Order order, int productId);
    List<Order> getAll();
    List<Order> getBy(int userId);
    Order getActual(int sessionId);
}
