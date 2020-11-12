package com.codecool.shop;

import com.codecool.shop.dao.dao.DataDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ProductDaoJdbcTest {

    ProductDao productDao;
    DataDao dataDao;
    ProductCategory testCategory;
    Supplier testSupplier;
    Supplier testSupplier2;
    List<Product> dbProducts = new ArrayList<>();
    List<Product> FilteredDbProducts = new ArrayList<>();

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
        testSupplier2 = new Supplier("supplier_2", "suppl_description_2");
        testSupplier2.setId(2);
        dataDao.createTable();
        dataDao.addDataForProductTest();
        updateDbProducts();
        updateFilteredDbProducts();
    }

//    Helper functions to create lists with products that are expected to be retrieved from database
//
    private void updateDbProducts () {
        for (int prodIndex =1; prodIndex <5; prodIndex++){
            addProductToTestList (testSupplier, testCategory,
                    dbProducts, prodIndex);
        }
        addProductToTestList(testSupplier2, testCategory, dbProducts, 5);
    }

    private void updateFilteredDbProducts(){
        addProductToTestList(testSupplier2, testCategory, FilteredDbProducts, 5);
    }

    private void addProductToTestList (Supplier supplier, ProductCategory category,
                                       List<Product> productsList, int prodIndex) {
        Product dbProduct;
        dbProduct = new Product("prod_" + prodIndex, 100 * prodIndex,
                "product_" + prodIndex + ".jpg", "prod_desc_" + prodIndex,
                category, supplier);
        dbProduct.setId(prodIndex);
        productsList.add(dbProduct);
    }


//    Tests

    @org.junit.jupiter.api.Test
    void add() throws SQLException {
        Product testProduct = new Product("test", 1000, "test_pic.jpg",
                "test product", testCategory, testSupplier);
        productDao.add(testProduct);
        Product addedProduct = productDao.find(6);
        assertEquals(testProduct.toString(), addedProduct.toString(), "Product not added" );
    }

    @org.junit.jupiter.api.Test
    void findExistingProduct() {
        Product foundProduct = productDao.find(4);
        assertEquals(4, foundProduct.getId(), "Product not found");
    }

    @org.junit.jupiter.api.Test
    void findNotExistingProduct() {
        Product foundProduct = productDao.find(109990);
        assertEquals(null, foundProduct, "Product not found");
    }

    @org.junit.jupiter.api.Test
    void remove() {
        productDao.remove(1);
        Product foundProduct = productDao.find(1);
        assertEquals(null, foundProduct, "Error with deleting product");
    }

    @org.junit.jupiter.api.Test
    void getAll() {
        assertEquals(dbProducts.toString(), productDao.getAll().toString(),
                "Products not matching added products");
    }

    @org.junit.jupiter.api.Test
    void getBySupplier() {
        assertEquals(FilteredDbProducts.toString(), productDao.getBy(testSupplier2).toString(),
                "Products not matching");
    }

    @org.junit.jupiter.api.Test
    void GetByCategory() {
        assertEquals(dbProducts.toString(), productDao.getBy(testCategory).toString(),
                "Products not matching");
    }
}