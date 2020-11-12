package com.codecool.shop.dao.dao;

import com.codecool.shop.dao.dao.DataDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;
import jdk.jfr.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ProductCategoryDaoJdbcTest {

	ProductCategoryDao categoryDao;
	DataDao dataDao;
	List<ProductCategory> categories = new ArrayList<>();

	@BeforeEach
	public void initializeTestDataBase () throws SQLException {
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		databaseManager.run("db_test_config.properties");
		categoryDao = databaseManager.categoryDao;
		dataDao = databaseManager.dataDao;
		dataDao.createTable();
		dataDao.addDataForProductTest();
	}

	@org.junit.jupiter.api.Test
	@DisplayName("Add category to database")
	void add() {
		ProductCategory testCategory = new ProductCategory("test_name",
				"test_department", "test_description");
		categoryDao.add(testCategory);
		ProductCategory addedCategory = categoryDao.find(2);
		assertEquals(testCategory.toString(), addedCategory.toString());
	}

	@org.junit.jupiter.api.Test
	@DisplayName("Find existing category by id")
	void findExistingCategory() {
		ProductCategory addedCategory = categoryDao.find(1);
		assertEquals(1, addedCategory.getId());
	}

	@org.junit.jupiter.api.Test
	@DisplayName("Find not existing category by id")
	void findNotExistingCategory() {
		ProductCategory addedCategory = categoryDao.find(10);
		assertNull(addedCategory);
	}

	@org.junit.jupiter.api.Test
	@DisplayName("Find existing category by name")
	void getExistingCategoryByName() {
		String name = "cat_1";
		ProductCategory category = categoryDao.getCategoryByName(name);
		assertEquals(name, category.getName());
	}

	@org.junit.jupiter.api.Test
	@DisplayName("Find not existing category by name")
	void getNotExistingCategoryByName() {
		String name = "cat_123";
		ProductCategory category = categoryDao.getCategoryByName(name);
		assertNull(category);
	}


	@org.junit.jupiter.api.Test
	@DisplayName("Delete existing value from database")
	void removeExistingCategory() {
		categoryDao.remove(1);
		assertNull(categoryDao.find(1));
	}

	@org.junit.jupiter.api.Test
	@DisplayName("Get all categories as list")
	void getAll() {
		categories = categoryDao.getAll();
		assertEquals(1, categories.size());
	}
}
