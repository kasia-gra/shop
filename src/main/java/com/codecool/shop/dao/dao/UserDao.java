package com.codecool.shop.dao.dao;

import com.codecool.shop.model.user.Address;
import com.codecool.shop.model.user.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User find(int id);
    void update(User user);
    void remove(int id);
    List<User> getAll();
    User findUserByEmail(String eMail);
}
