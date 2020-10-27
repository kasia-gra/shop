package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDaoMem implements UserDao {
    private int sequenceNumber = 1;
    private List<User> data = new ArrayList<>();
    private static UserDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private UserDaoMem() {
    }

    public static UserDaoMem getInstance() {
        if (instance == null) {
            instance = new UserDaoMem();
        }
        return instance;
    }

    @Override
    public int add(User user) {
        user.setId(sequenceNumber);
        data.add(user);
        sequenceNumber++;
        return user.getId();
    }

    @Override
    public User find(int id) {
        return data.stream().
                filter(user -> user.getId() == id).
                findFirst().
                orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<User> getAll() {
        return data;
    }
}
