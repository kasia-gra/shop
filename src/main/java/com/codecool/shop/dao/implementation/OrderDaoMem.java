package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDaoMem implements OrderDao {
    private int sequenceNumber = 1;
    private List<Order> data = new ArrayList<>();
    private static OrderDaoMem instance = null;


    /* A private Constructor prevents any other class from instantiating.
     */
    private OrderDaoMem() {
    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public int add(Order order) {
        order.setId(sequenceNumber);
        data.add(order);
        sequenceNumber++;
        return order.getId();
    }

    @Override
    public Order find(int id) {
        return data.stream().
                filter(order -> order.getId() == id).
                findFirst().
                orElse(null);
    }

    @Override
    public void update(int id) {

    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Order> getAll() {
        return data;
    }

    @Override
    public List<Order> getBy(int userId) {
        return data.stream().
                filter(order -> order.getUser().getId() == userId).
                collect(Collectors.toList());
    }

    @Override
    public Order getActual(int userId) {
        return data.stream().
                filter(order -> order.getUser().getId() == userId).
                filter(order -> !order.isArchival()).
                findFirst().
                orElse(null);
    }
}
