package com.codecool.shop.dao.dao;

import com.codecool.shop.model.Session;

import java.util.List;

public interface SessionDao {
    void add(Session session);
    void remove(int id);
    List<Session> getAll();
}
