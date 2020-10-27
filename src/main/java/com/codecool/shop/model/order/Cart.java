package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;

import java.util.HashMap;

public class Cart {
    private int id;
    private HashMap<Product, Integer> products;

    public Cart() {
        this.products = new HashMap<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }
}
