package com.codecool.shop.dao.dao;

import com.codecool.shop.model.user.User;

import java.util.List;

public interface UserDao {
    int add(User user);
    User find(int id);
    void remove(int id);

    List<User> getAll();
}
