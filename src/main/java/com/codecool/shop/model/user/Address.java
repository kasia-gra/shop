package com.codecool.shop.model.user;

public class Address {
    private int id;

    private String country;
    private String city;
    private String zipCode;
    private String address;
    public Address(String country, String city, String zipCode, String address) {
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
    }
}
