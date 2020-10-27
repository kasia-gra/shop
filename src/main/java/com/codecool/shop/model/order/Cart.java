package com.codecool.shop.model.order;

import java.util.ArrayList;

public class Cart {
    private int orderNumber = 1;
    private final ArrayList<Order> orders = new ArrayList<>();
    private static Cart instance = null;

    private Cart(){
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addOrder(Order order) {
        order.setId(orderNumber);
        orders.add(order);
        orderNumber++;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Order getOrder(int id) {
        return orders.stream().
                filter(order -> order.getId() == id).
                findFirst().
                orElse(null);
    }

    public void removeOrder(int id) {
        orders.remove(getOrder(id));
    }

}
