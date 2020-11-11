package com.codecool.shop.model.user;

import com.codecool.shop.model.AddressDetail;

public class User {
    private int id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private AddressDetail userAddressDetail;

    public User() {
    }

    public User(String email, String phone, String firstName, String lastName, AddressDetail userAddressDetail) {
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAddressDetail = userAddressDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AddressDetail getUserAddressDetail() {
        return userAddressDetail;
    }

    public void setUserAddressDetail(AddressDetail userAddressDetail) {
        this.userAddressDetail = userAddressDetail;
    }
}
