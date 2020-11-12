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
    ProductCategory testCategory;
    Supplier testSupplier;


    @BeforeEach
    public void initializeTestDataBase () throws SQLException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        databaseManager.run("db_test_config.properties");
        productDao =  databaseManager.productDao;
        dataDao = databaseManager.dataDao;
        testCategory = new ProductCategory("cat_1",  "cat_dept_1", "cat_description_1");
        testCategory.setId(1);
        testSupplier = new Supplier("supplier_1", "suppl_description_1");
        testSupplier.setId(1);
        dataDao.createTable();
        dataDao.addDataForProductTest();
    }

    @org.junit.jupiter.api.Test
    void add() throws SQLException {
        Product testProduct = new Product("test", 1000, "test_pic.jpg",
                "test product", testCategory, testSupplier);
        productDao.add(testProduct);
        Product addedProduct = productDao.find(5);
        assertEquals(testProduct.toString(), addedProduct.toString(), "Supplier not added" );
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