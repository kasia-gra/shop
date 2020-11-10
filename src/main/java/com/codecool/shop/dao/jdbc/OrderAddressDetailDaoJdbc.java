package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.AddressDao;
import com.codecool.shop.dao.dao.OrderAddressDetailDao;
import com.codecool.shop.model.order.OrderAddressDetail;
import com.codecool.shop.model.user.Address;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderAddressDetailDaoJdbc implements OrderAddressDetailDao {
    private DataSource dataSource;
    private AddressDao addressDao;

    public OrderAddressDetailDaoJdbc(DataSource dataSource, AddressDao addressDao) {
        this.dataSource = dataSource;
        this.addressDao = addressDao;
    }

    @Override
    public void add(OrderAddressDetail orderAddressDetail) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO order_address_detail (order_billing_address_id, order_shipping_address_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, orderAddressDetail.getBillingAddress().getId());
            statement.setInt(2, orderAddressDetail.getShippingAddress().getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            orderAddressDetail.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new order address detail.", exception);
        }
    }

    @Override
    public OrderAddressDetail find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM order_address_detail WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            Address orderBillingAddress = addressDao.find(resultSet.getInt("order_billing_address_id"));
            Address orderShippingAddress = addressDao.find(resultSet.getInt("order_shipping_address_id"));

            OrderAddressDetail orderAddressDetail = new OrderAddressDetail(orderBillingAddress, orderShippingAddress);
            orderAddressDetail.setId(id);

            return orderAddressDetail;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving order address detail with id: " + id, exception);
        }
    }

    @Override
    public void update(OrderAddressDetail orderAddressDetail) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE order_address_detail SET order_billing_address_id = ?, order_shipping_address_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, orderAddressDetail.getBillingAddress().getId());
            statement.setInt(2, orderAddressDetail.getShippingAddress().getId());
            statement.setInt(3, orderAddressDetail.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating order address detail with id: " + orderAddressDetail.getId(), exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM order_address_detail WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting order address detail with id: " + id, exception);
        }
    }

    @Override
    public List<OrderAddressDetail> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM order_address_detail";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<OrderAddressDetail> orderAddressDetailList = new ArrayList<>();
            while (resultSet.next()) {
                Address orderBillingAddress = addressDao.find(resultSet.getInt("order_billing_address_id"));
                Address orderShippingAddress = addressDao.find(resultSet.getInt("order_shipping_address_id"));

                OrderAddressDetail orderAddressDetail = new OrderAddressDetail(orderBillingAddress, orderShippingAddress);
                orderAddressDetail.setId(resultSet.getInt("id"));
                orderAddressDetailList.add(orderAddressDetail);
            }
            return orderAddressDetailList;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving all order address details.", exception);
        }
    }
}
