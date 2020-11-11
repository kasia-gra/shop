package com.codecool.shop.model.order;

import com.codecool.shop.model.product.Product;

public class LineItem {

    Product product;
    int qty;
    float linePrice;
    int lineId;
    int cartId;

    public LineItem(Product product, int qty, int cartId) {
        this.product = product;
        this.qty = qty;
        this.cartId = cartId;
    }

    public int getCartId(){
        return this.cartId;
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

    public void setLineId (int lineId){
        this.lineId = lineId;
    }

    @Override
    public String toString() {
        return "[Product: " + product.getName() + ", quantity: " + qty + "]";
    }
}


