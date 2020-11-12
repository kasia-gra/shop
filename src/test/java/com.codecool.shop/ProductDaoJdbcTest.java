package com.codecool.shop;

import com.codecool.shop.dao.manager.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

class ProductDaoJdbcTest {


    @BeforeEach
    public void initializeTestDataBase () throws SQLException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        databaseManager.run("db_test_config.properties");
    }

    @org.junit.jupiter.api.Test
    void add() throws InterruptedException {

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