package com.codecool.shop.dao.dao;

import com.codecool.shop.model.Logfile;

public interface LogfileDao {
    void add(Logfile logfile);
    Logfile find(int id);
    Logfile getBy(int orderId);
}
