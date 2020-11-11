package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Order;

import java.util.List;

public interface OrderDao {
    void add(Order order, int productId);
    Order find(int id);
    void update(int id);
    void remove(int id);

    List<Order> getAll();
    List<Order> getBy(int userId);
    Order getActual(int sessionId);
}
