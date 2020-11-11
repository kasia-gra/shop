package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.ProductCategoryDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.SupplierDao;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductDaoJdbc implements ProductDao {
	private final DataSource dataSource;
	private final SupplierDao supplierDao;
	private final ProductCategoryDao categoryDao;


	public ProductDaoJdbc(DataSource dataSource, SupplierDao supplierDao, ProductCategoryDao categoryDao) {
		this.dataSource = dataSource;
		this.supplierDao = supplierDao;
		this.categoryDao = categoryDao;
	}

	@Override
	public void add(Product product) {
		try (Connection conn = dataSource.getConnection()) {
			String sql = "INSERT INTO product (name, default_price, picture_name," +
					" description, category_id, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, product.getName());
			st.setFloat(2, product.getDefaultPrice());
			st.setString(3, product.getPictureName());
			st.setString(4, product.getDescription());
			st.setInt(5, product.getProductCategory().getId());
			st.setInt(6, product.getSupplier().getId());
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			product.setId(rs.getInt(1));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Product find(int id) {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT * FROM product WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();

			if (!rs.next()) { return null; }
			Supplier supplier = supplierDao.find(rs.getInt("supplier_id"));
			ProductCategory category = categoryDao.find(rs.getInt("category_id"));

			Product product = new Product(rs.getString("name"), rs.getFloat("default_price"),
					rs.getString("picture_name"), rs.getString("description"), category, supplier);
			product.setId(id);
			return product;
		}
		catch (SQLException e) {
			throw new RuntimeException("Error while reading product", e);
		}
	}

	@Override
	public void remove(int id) {
		try (Connection conn = dataSource.getConnection()) {
			String sql = "DELETE from product WHERE id = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Product> getAll() {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT * FROM product";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			List<Product> products = new ArrayList<>();
			while (rs.next()) {
				Supplier supplier = supplierDao.find(rs.getInt("supplier_id"));
				ProductCategory category = categoryDao.find(rs.getInt("category_id"));
				Product product = new Product(rs.getString("name"), rs.getFloat("default_price"),
						rs.getString("picture_name"), rs.getString("description"), category, supplier);
				product.setId(rs.getInt("id"));
				products.add(product);
			}
			return products;

		} catch (SQLException e) {
			throw new RuntimeException("Error while reading products", e);
		}
	}

	@Override
	public List<Product> getBy(Supplier supplier) {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT * FROM product WHERE supplier_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, supplier.getId());
			ResultSet rs = statement.executeQuery();
			List<Product> products = new ArrayList<>();

			while (rs.next()) {	//TODO think about refactor duplicated code
				ProductCategory category = categoryDao.find(rs.getInt("category_id"));
				Product product = new Product(rs.getString("name"), rs.getFloat("default_price"),
						rs.getString("picture_name"), rs.getString("description"), category, supplier);
				product.setId(rs.getInt("id"));
				products.add(product);
			}
			return products;

		} catch (SQLException e) {
			throw new RuntimeException("Error while reading products by supplier", e);
		}
	}

	@Override
	public List<Product> getBy(ProductCategory category) {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT * FROM product WHERE category_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, category.getId());
			ResultSet rs = statement.executeQuery();
			List<Product> products = new ArrayList<>();

			while (rs.next()) {
				Supplier supplier = supplierDao.find(rs.getInt("supplier_id"));
				Product product = new Product(rs.getString("name"), rs.getFloat("default_price"),
						rs.getString("picture_name"), rs.getString("description"), category, supplier);
				product.setId(rs.getInt("id"));
				products.add(product);
			}
			return products;

		} catch (SQLException e) {
			throw new RuntimeException("Error while reading products by category", e);
		}
	}
}
