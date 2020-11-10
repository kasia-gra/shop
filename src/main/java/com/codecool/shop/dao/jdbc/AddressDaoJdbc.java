package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.AddressDao;
import com.codecool.shop.model.user.Address;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDaoJdbc implements AddressDao {
    private final DataSource dataSource;

    public AddressDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Address address) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO address (country, city, zip_code, address) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, address.getCountry());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getZipCode());
            statement.setString(4, address.getAddress());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            address.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new address.", exception);
        }
    }

    @Override
    public Address find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM address WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                return null;
            }
            return new Address(resultSet.getString("country"), resultSet.getString("city"),
                    resultSet.getString("zip_code"), resultSet.getString("address"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving address with id: " + id, exception);
        }
    }

    @Override
    public void update(Address address) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE address SET country = ?, city = ?, zip_code = ?, address = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address.getCountry());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getZipCode());
            statement.setString(4, address.getAddress());
            statement.setInt(5, address.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating address with id: " + address.getId(), exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM address WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting address with id: " + id, exception);
        }
    }

    @Override
    public List<Address> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM address";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<Address> addresses = new ArrayList<>();
            while (resultSet.next()) {
                Address address = new Address(resultSet.getString("country"), resultSet.getString("city"),
                        resultSet.getString("zip_code"), resultSet.getString("address"));
                address.setId(resultSet.getInt("id"));
                addresses.add(address);
            }
            return addresses;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving all addresses.", exception);
        }
    }
}
