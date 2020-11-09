package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.model.user.User;

import java.util.List;

public class UserDaoJdbc implements UserDao {
    @Override
    public int add(User user) {
        return 0;
    }

    @Override
    public User find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
