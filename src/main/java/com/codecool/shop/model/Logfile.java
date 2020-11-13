package com.codecool.shop.model;

public class Logfile {
    private int id;
    private int orderId;
    private String filename;

    public Logfile(int orderId, String filename) {
        this.orderId = orderId;
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
