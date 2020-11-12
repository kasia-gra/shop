package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.Cart;

import java.sql.SQLException;

public interface DataDao {
    void createTable()  throws SQLException;
    void addData();
}
