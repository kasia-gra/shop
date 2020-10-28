package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private int id;
    private Map<Product, Integer> products;

    public Cart() {
        this.products = new HashMap<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
    }

    public void removeProduct(Product product) {
        if (products.containsKey(product) && products.get(product) != 1) {
            products.replace(product, products.get(product) - 1);
        } else if (products.containsKey(product) && products.get(product) == 1) {
            products.remove(product);
        }
    }

    public int getSize() {
        return products.values().stream().
                reduce(0, Integer::sum);
    }

    public float getTotalPrice() {
        return products.keySet().stream().
                map(Product::getDefaultPrice).
                reduce(0.0f, Float::sum);
    }
}
