package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.product.ProductCategory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private DataSource dataSource;

    public ProductCategoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ProductCategory category) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO category (name, department, description) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDepartment());
            statement.setString(3, category.getDescription());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            category.setId(rs.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ProductCategory find(int id) {

        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM category WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.next()) {
                return null;
            }
            return new ProductCategory(rs.getString("name"),
                    rs.getString("department"), rs.getString("description"));

        } catch (SQLException e) {
            throw new RuntimeException("Error while reading category", e);
        }

    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE from category WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, 1);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductCategory> getAll() {

        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM category";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<ProductCategory> categories = new ArrayList<>();
            while (rs.next()) {
                ProductCategory category = new ProductCategory(rs.getString("name"),
                        rs.getString("department"), rs.getString("description"));
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading categories", e);
        }

    }
}
