package com.codecool.shop.dao.jdbc;

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
			String sql = "SELECT * FROM product";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) { //temporary
				ProductCategory cat = new ProductCategory("TESTs", "test", "A tablet");
				Supplier sup = new Supplier("TEST", "TEST");
				Product product = new Product(rs.getString("name"), rs.getFloat("default_price"),
						"USD", rs.getString("description"), cat, sup);
				product.setId(rs.getInt(1));
				data.add(product);
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
