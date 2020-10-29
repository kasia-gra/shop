package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;

public class LineItem {

    Product product;
    int qty;
    float linePrice;
    int lineId;

    LineItem(Product product, int qty, int id) {
        this.product = product;
        this.qty = qty;
        this.lineId = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getLinePrice() {
        return product.getDefaultPrice() * qty;
    }
    public void setLinePrice(float linePrice) {
        this.linePrice = linePrice;
    }

    public int getLineId () {
        return this.lineId;
    }
}


