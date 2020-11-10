package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.SupplierDao;
import com.codecool.shop.model.product.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao {

    private DataSource dataSource;
    private List<Supplier> data = new ArrayList<>();

    public SupplierDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Supplier supplier) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO supplier (name, description) VALUES (?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, supplier.getName());
            st.setString(2, supplier.getDescription());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            supplier.setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sqlQuery = "SELECT * FROM supplier WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sqlQuery);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String name = rs.getString(2);
            String description = rs.getString(3);
            Supplier supplier = new Supplier(name, description);
            supplier.setId(id);
            return supplier;
        }
        catch (SQLException e) {
            throw  new RuntimeException("Error getting Supplier by id");
        }
    }

    @Override
    public Supplier getSupplierByName(String supplierName) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM supplier WHERE name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, supplierName);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Supplier supplier = new Supplier(supplierName, rs.getString("description"));
            supplier.setId(rs.getInt("id"));
            return supplier;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error while reading supplier by name", e);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE from supplier WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Supplier> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM supplier";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Supplier supplier = new Supplier(rs.getString("name"), rs.getString("description"));
                supplier.setId(rs.getInt(1));
                data.add(supplier);
            }
            return data;
        } catch (SQLException e) {
            throw new RuntimeException("ERROR while reading suppliers " + e.getMessage());
        }
    }
}
