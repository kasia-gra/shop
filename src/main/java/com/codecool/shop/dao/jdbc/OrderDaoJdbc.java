package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.model.order.Order;

import java.util.List;

public class OrderDaoJdbc implements OrderDao {
    @Override
    public int add(Order order) {
        return 0;
    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public void update(int id) {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public List<Order> getBy(int userId) {
        return null;
    }

    @Override
    public Order getActual(int userId) {
        return null;
    }
}
