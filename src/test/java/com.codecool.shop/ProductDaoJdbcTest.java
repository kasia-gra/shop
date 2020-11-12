package com.codecool.shop;

import com.codecool.shop.dao.dao.DataDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;
import jdk.jfr.Category;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

class ProductDaoJdbcTest {


    ProductDao productDao;
    DataDao dataDao;

    @BeforeEach
    public void initializeTestDataBase () throws SQLException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        databaseManager.run("db_test_config.properties");
        productDao =  databaseManager.productDao;
        dataDao = databaseManager.dataDao;
    }

    @org.junit.jupiter.api.Test
    void add() throws SQLException {
        dataDao.createTable();
        dataDao.addData();
        ProductCategory testCategory = new ProductCategory("test_category",  "test_department", "test_description");
        Supplier testSupplier = new Supplier("test_supplier", "test_description");
        Product testProduct = new Product("test", 1000, "test_pic.jpg",
                "test product", testCategory, testSupplier);
        productDao.add(testProduct);
        Product addedProduct = productDao.find(1);
        assertEquals(testProduct.getPictureName(), addedProduct.getPictureName(), "Supplier not added" );
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