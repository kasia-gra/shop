package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.AddressDao;
import com.codecool.shop.dao.dao.AddressDetailDao;
import com.codecool.shop.model.AddressDetail;
import com.codecool.shop.model.user.Address;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDetailDaoJdbc implements AddressDetailDao {
    private final DataSource dataSource;
    private final AddressDao addressDao;

    public AddressDetailDaoJdbc(DataSource dataSource, AddressDao addressDao) {
        this.dataSource = dataSource;
        this.addressDao = addressDao;
    }

    @Override
    public void add(AddressDetail addressDetail) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO address_detail (billing_address_id, shipping_address_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            addressDao.add(addressDetail.getBillingAddress());
            addressDao.add(addressDetail.getShippingAddress());

            statement.setInt(1, addressDetail.getBillingAddress().getId());
            statement.setInt(2, addressDetail.getShippingAddress().getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            addressDetail.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new address detail.", exception);
        }
    }

    @Override
    public AddressDetail find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM address_detail WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            Address orderBillingAddress = addressDao.find(resultSet.getInt("billing_address_id"));
            Address orderShippingAddress = addressDao.find(resultSet.getInt("shipping_address_id"));

            AddressDetail addressDetail = new AddressDetail(orderBillingAddress, orderShippingAddress);
            addressDetail.setId(id);

            return addressDetail;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving address detail with id: " + id, exception);
        }
    }

    @Override
    public void update(AddressDetail addressDetail) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE address_detail SET billing_address_id = ?, shipping_address_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, addressDetail.getBillingAddress().getId());
            statement.setInt(2, addressDetail.getShippingAddress().getId());
            statement.setInt(3, addressDetail.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating address detail with id: " + addressDetail.getId(), exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM address_detail WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting address detail with id: " + id, exception);
        }
    }

    @Override
    public List<AddressDetail> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM address_detail";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<AddressDetail> addressDetailList = new ArrayList<>();
            while (resultSet.next()) {
                Address orderBillingAddress = addressDao.find(resultSet.getInt("billing_address_id"));
                Address orderShippingAddress = addressDao.find(resultSet.getInt("shipping_address_id"));

                AddressDetail addressDetail = new AddressDetail(orderBillingAddress, orderShippingAddress);
                addressDetail.setId(resultSet.getInt("id"));
                addressDetailList.add(addressDetail);
            }
            return addressDetailList;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving all address details.", exception);
        }
    }
}
