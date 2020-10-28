package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;

import java.util.*;
import java.util.stream.Collectors;

public class Cart {
    private int id;
    private Map<Product, Integer> products;
    private List<LineItem> lineItems;

    public Cart() {

        this.products = new HashMap<>();
        this.lineItems = new ArrayList<>();
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
        return products.entrySet().stream().
                map(entry -> entry.getKey().getDefaultPrice() * entry.getValue()).
                reduce(0.0f, Float::sum);
    }


    public void addLineItem(Product product) {
        List<LineItem> searchedLineItems = lineItems.stream()
                .filter(lineItem -> lineItem.getProduct().equals(product))
                .collect(Collectors.toList());
        if (searchedLineItems.size() == 0) {
            lineItems.add(new LineItem(product, 1));
        } else {
            LineItem searchedLineItem = searchedLineItems.get(0);
            searchedLineItem.setQty(searchedLineItem.getQty() + 1);
        }
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public int getNumOfLines() {
        return lineItems.size();
    }

    public int getCartSize() {
        return lineItems.stream()
                .map(lineItem -> lineItem.getQty())
                .reduce(0,Integer:: sum);
    }

    public float getLineItemsTotalPrice() {
        return lineItems.stream()
                .map(lineItem -> lineItem.getLinePrice())
                .reduce(0.0f, Float::sum);
    }

    public String getCartCurrency(){
        String currency = (lineItems.size() > 0) ? lineItems.get(0).getProduct().getDefaultCurrency().getCurrencyCode() : "-";
        return currency;
    }
}
