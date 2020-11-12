package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.*;
import com.codecool.shop.model.AddressDetail;
import com.codecool.shop.model.Session;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.order.Status;
import com.codecool.shop.model.user.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDaoJdbc implements OrderDao {
    private final DataSource dataSource;
    private final CartDao cartDao;
    private final SessionDao sessionDao;
    private final UserDao userDao;
    private final AddressDetailDao addressDetailDao;

    public OrderDaoJdbc(DataSource dataSource, CartDao cartDao, SessionDao sessionDao, UserDao userDao, AddressDetailDao addressDetailDao) {
        this.dataSource = dataSource;
        this.cartDao = cartDao;
        this.sessionDao = sessionDao;
        this.userDao = userDao;
        this.addressDetailDao = addressDetailDao;
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
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"order\" WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            Cart cart = cartDao.find(resultSet.getInt("cart_id"));
            Session session = sessionDao.find(resultSet.getInt("session_id"));
            Order order = new Order(cart, session);

            setOrderOptionalParameters(resultSet, order);

            order.setId(id);
            return order;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving order with id: " + id, exception);
        }
    }

    @Override
    public void update(Order order) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE \"order\" SET cart_id = ?, session_id = ?," +
                    "user_id = ?, order_address_details_id = ?, date = ?, current_status = ? " +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getCart().getId());
            statement.setInt(2, order.getSession().getId());
            statement.setInt(7, order.getId());

            setStatementOptionalParameters(statement, order);

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating order with id: " + order.getId(), exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM \"order\" WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting order with id: " + id, exception);
        }
    }

    @Override
    public void removeItem(int productId, Cart cart) {
        cartDao.removeItemFromCart(productId, cart);
    }

    @Override
    public void addItemToOrder(Order order, int productId, int addedQuantity) {
        cartDao.addItemToCart(order.getCart(), productId, addedQuantity);
    }

    @Override
    public List<Order> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"order\"";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Cart cart = cartDao.find(resultSet.getInt("cart_id"));
                Session session = sessionDao.find(resultSet.getInt("session_id"));
                Order order = new Order(cart, session);

                setOrderOptionalParameters(resultSet, order);

                order.setId(resultSet.getInt("id"));
                orders.add(order);
            }
            return orders;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving all orders.", exception);
        }
    }

    @Override
    public List<Order> getBy(int userId) {
        return null;
    }

    @Override
    public Order getActual(int sessionId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"order\" WHERE session_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, sessionId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Cart cart = cartDao.find(resultSet.getInt("cart_id"));
            Session session = sessionDao.find(resultSet.getInt("session_id"));
            Order order = new Order(cart, session);

            setOrderOptionalParameters(resultSet, order);
            order.setId(resultSet.getInt("id"));
            return order;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving order with session id: " + sessionId, exception);
        }
    }



    private void setOrderOptionalParameters(ResultSet resultSet, Order order) throws SQLException {
        if (resultSet.getInt("user_id") != 0) {
            User user = userDao.find(resultSet.getInt("user_id"));
            order.setUser(user);
        }
        if (resultSet.getInt("order_address_details_id") != 0) {
            AddressDetail addressDetail = addressDetailDao.find(resultSet.getInt("order_address_details_id"));
            order.setOrderAddressDetail(addressDetail);
        }
        if (resultSet.getDate("date") != null) {
            Date date = resultSet.getDate("date");
            order.setDate(date);
        }
        if (resultSet.getString("current_status") != null) {
            Status status = resultSet.getObject("current_status", Status.class);
            order.setStatus(status);
        }
    }

    private void setStatementOptionalParameters(PreparedStatement statement, Order order) throws SQLException {
        if (order.getUser() != null) statement.setInt(3, order.getUser().getId());
        else statement.setNull(3, Types.INTEGER);
        if (order.getOrderAddressDetail() != null) statement.setInt(4, order.getOrderAddressDetail().getId());
        else statement.setNull(4, Types.INTEGER);
        if (order.getDate() != null) statement.setDate(5, new java.sql.Date(order.getDate().getTime()));
        else statement.setNull(5, Types.DATE);
        if (order.getStatus() != null) statement.setObject(6, order.getStatus());
        else statement.setObject(6, null);
    }
}
