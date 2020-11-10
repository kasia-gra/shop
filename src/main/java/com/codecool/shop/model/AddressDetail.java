package com.codecool.shop.model;

import com.codecool.shop.model.user.Address;

public class AddressDetail {
    private int id;
    private Address billingAddress;
    private Address shippingAddress;

    public AddressDetail(Address billingAddress, Address shippingAddress) {
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
