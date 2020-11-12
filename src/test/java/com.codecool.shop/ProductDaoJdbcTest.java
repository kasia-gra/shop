package com.codecool.shop;

import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

class ProductDaoJdbcTest {


    ProductDao productDao;

    @BeforeEach
    public void initializeTestDataBase () throws SQLException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        databaseManager.run("db_test_config.properties");
        productDao =  databaseManager.productDao;
    }

    @org.junit.jupiter.api.Test
    void add() throws InterruptedException {
        productDao.find(1);
    }

    @org.junit.jupiter.api.Test
    void find() {
    }

    @org.junit.jupiter.api.Test
    void remove() {
    }

    @org.junit.jupiter.api.Test
    void getAll() {
    }

    @org.junit.jupiter.api.Test
    void getBy() {
    }

    @org.junit.jupiter.api.Test
    void testGetBy() {
    }
}