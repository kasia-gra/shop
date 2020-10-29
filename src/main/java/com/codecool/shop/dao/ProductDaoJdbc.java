package com.codecool.shop.dao;

import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//TODO NOT WORKING YET

public class ProductDaoJdbc implements ProductDao {
	private DataSource dataSource;
	private List<Product> data = new ArrayList<>();

	public ProductDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void add(Product product) {

	}

	@Override
	public Product find(int id) {
		return null;
	}

	@Override
	public void remove(int id) {

	}

	@Override
	public List<Product> getAll() {
		try (Connection conn = dataSource.getConnection()) {
			String sql = "SELECT * FROM products";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) { //temporary
				ProductCategory cat = new ProductCategory("TESTs", "test", "A tablet");
				Supplier sup = new Supplier("TEST", "TEST");
				data.add(new Product(rs.getString(2), rs.getFloat(3), rs.getString(4), rs.getString(5), cat, sup));
			}
			return data;
		} catch (SQLException e) {
			throw new RuntimeException("ERROR while reading products " + e.getMessage());
		}
	}

	@Override
	public List<Product> getBy(Supplier supplier) {
		return null;
	}

	@Override
	public List<Product> getBy(ProductCategory productCategory) {
		return null;
	}
}