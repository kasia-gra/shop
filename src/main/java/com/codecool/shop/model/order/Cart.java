package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cart {
    private int id;
    private List<LineItem> lineItems;

    private float totalPrice;
    private int size;

    public Cart() {
        this.totalPrice = 0;
        this.size = 0;
        this.lineItems = new ArrayList<>();
    }

    public Cart(float totalPrice, int size, List<LineItem> lineItems) {
        this.totalPrice = totalPrice;
        this.size = size;
        this.lineItems = lineItems;
    }

    public float getTotalPrice (){
        return this.totalPrice;
    }

    public int getSize(){
        return this.size;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void addLineItem(Product product) {
        List<LineItem> searchedLineItems = lineItems.stream()
                .filter(lineItem -> lineItem.getProduct().equals(product))
                .collect(Collectors.toList());
        if (searchedLineItems.size() == 0) {
            lineItems.add(new LineItem(product, 1, lineItems.size()));
        } else {
            LineItem searchedLineItem = searchedLineItems.get(0);
            searchedLineItem.setQty(searchedLineItem.getQty() + 1);
        }
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }


    public int getCartSize() {
        return lineItems.stream()
                .map(lineItem -> lineItem.getQty())
                .reduce(0, Integer::sum);
    }

    public float getLineItemsTotalPrice() {
        return lineItems.stream()
                .map(lineItem -> lineItem.getLinePrice())
                .reduce(0.0f, Float::sum);
    }

    public String getCartCurrency() {
        String currency = (lineItems.size() > 0) ? lineItems.get(0).getProduct().getDefaultCurrency().getCurrencyCode() : "-";
        return currency;
    }

    public void removeLineItemById(int searchedId) throws IllegalStateException {
        lineItems.remove(getLineItemById(searchedId));
    }

    public LineItem getLineItemById (int searchedId) throws IllegalStateException {
        List<LineItem> searchedLineItems = lineItems.stream()
                .filter(lineItem -> lineItem.getLineId() == searchedId)
                .collect(Collectors.toList());
        if (searchedLineItems.size() > 0) {
            return searchedLineItems.get(0);
        }
        else {
            throw new IllegalStateException();
        }
    }

}
