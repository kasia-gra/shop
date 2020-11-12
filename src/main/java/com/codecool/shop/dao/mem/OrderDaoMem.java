package com.codecool.shop.dao.mem;

import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.model.order.Cart;
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
    public void add(Order order, int productId) {
        order.setId(sequenceNumber);
        data.add(order);
        sequenceNumber++;
    }

    @Override
    public Order find(int id) {
        return data.stream().
                filter(order -> order.getId() == id).
                findFirst().
                orElse(null);
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public void remove(Order order) {
        data.remove(find(order.getId()));
    }

    @Override
    public void removeItem(int productId, Cart cart){

    }

    @Override
    public void addItemToOrder(Order order, int productId, int addedQuantity) {

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
    public Order getActual(int sessionId) {
        return data.stream().
                filter(order -> order.getSession().getId() == sessionId).
                findFirst().
                orElse(null);
    }
}
