package com.codecool.shop.dao.dao;

import com.codecool.shop.model.user.Address;

import java.util.List;

public interface AddressDao {
    void add(Address address);
    Address find(int id);
    void remove(int id);
    List<Address> getAll();
}
