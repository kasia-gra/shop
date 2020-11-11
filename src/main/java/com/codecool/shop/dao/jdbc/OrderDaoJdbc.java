package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.CartDao;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.SessionDao;
import com.codecool.shop.model.order.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class OrderDaoJdbc implements OrderDao {
    private DataSource dataSource;
    private CartDao cartDao;
    private SessionDao sessionDao;

    public OrderDaoJdbc(DataSource dataSource, CartDao cartDao, SessionDao sessionDao) {
        this.dataSource = dataSource;
        this.cartDao = cartDao;
        this.sessionDao = sessionDao;
    }

    @Override
    public void add(Order order, int productId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO \"order\" (cart_id, session_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            cartDao.addEmptyCart(order.getCart(), productId);
            sessionDao.add(order.getSession());

            statement.setInt(1, order.getCart().getId());
            statement.setInt(2, order.getSession().getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            order.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new order.", exception);
        }
    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public void update(int id) {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public List<Order> getBy(int userId) {
        return null;
    }

    @Override
    public Order getActual(int sessionId) {
        return null;
    }
}
