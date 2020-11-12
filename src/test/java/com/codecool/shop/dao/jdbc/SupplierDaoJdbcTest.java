package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.DataDao;
import com.codecool.shop.dao.dao.SupplierDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierDaoJdbcTest {
    SupplierDao supplierDao;
    DataDao dataDao;

    @BeforeEach
    public void initializeTestDataBase () throws SQLException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        databaseManager.run("db_test_config.properties");
        supplierDao = databaseManager.supplierDao;
        dataDao = databaseManager.dataDao;
        dataDao.createTable();
        dataDao.addDataForProductTest();
    }

    @Test
    @Order(1)
    @DisplayName("Add supplier to database")
    void add() {
        //Arrange
        Supplier expectedSupplier = new Supplier("test_name", "test_description");
        //Act
        supplierDao.add(expectedSupplier);
        //Assert
        Supplier actualSupplier = supplierDao.find(3);
        assertEquals(expectedSupplier, actualSupplier);
    }

    @Test
    @Order(2)
    @DisplayName("Find existing supplier by id")
    void findExistingSupplier() {
        //Arrange
        Supplier expectedSupplier = new Supplier("supplier_1", "suppl_description_1");
        expectedSupplier.setId(1);
        //Act
        Supplier actualSupplier = supplierDao.find(1);
        //Assert
        assertEquals(expectedSupplier, actualSupplier);
    }

    @Test
    @Order(3)
    @DisplayName("Find not existing supplier by id")
    void findNotExistingSupplier() {
        //Arrange
        //Act
        Supplier actualSupplier = supplierDao.find(3);
        //Assert
        assertNull(actualSupplier);
    }

    @Test
    @Order(4)
    @DisplayName("Find existing supplier by name")
    void getExistingSupplierByName() {
        //Arrange
        String supplierName = "supplier_1";
        Supplier expectedSupplier = new Supplier(supplierName, "suppl_description_1");
        expectedSupplier.setId(1);
        //Act
        Supplier actualSupplier = supplierDao.getSupplierByName(supplierName);
        //Assert
        assertEquals(expectedSupplier, actualSupplier);
    }

    @Test
    @Order(5)
    @DisplayName("Find not existing supplier by name")
    void getNotExistingSupplierByName() {
        //Arrange
        String supplierName = "supplier_0";
        //Act
        Supplier actualSupplier = supplierDao.getSupplierByName(supplierName);
        //Assert
        assertNull(actualSupplier);
    }

    @Test
    @Order(6)
    @DisplayName("Remove existing supplier from database")
    void removeExistingSupplier() {
        //Arrange
        int removedSupplierId = 1;
        //Act
        supplierDao.remove(removedSupplierId);
        //Assert
        Supplier actualSupplier = supplierDao.find(removedSupplierId);
        assertNull(actualSupplier);
    }

    @Test
    @Order(7)
    @DisplayName("Get all suppliers as list")
    void getAll() {
        //Arrange
        Supplier supplier1 = new Supplier("supplier_1", "suppl_description_1");
        supplier1.setId(1);
        Supplier supplier2 = new Supplier("supplier_2", "suppl_description_2");
        supplier2.setId(2);
        List<Supplier> expectedSuppliers = new ArrayList<>(Arrays.asList(supplier1, supplier2));
        //Act
        List<Supplier> actualSuppliers = supplierDao.getAll();
        //Assert
        assertEquals(expectedSuppliers, actualSuppliers);
    }
}