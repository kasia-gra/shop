package com.codecool.shop.dao.mem;

import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.model.user.User;

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
    public void add(User user) {
        user.setId(sequenceNumber);
        data.add(user);
        sequenceNumber++;
    }

    @Override
    public User find(int id) {
        return data.stream().
                filter(user -> user.getId() == id).
                findFirst().
                orElse(null);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<User> getAll() {
        return data;
    }

    @Override
    public User findUserByEmail(String eMail) {
        return null;
    }

    @Override
    public User findUserByEmailAndPassword(String eMail, String password) {
        return null;
    }
}
