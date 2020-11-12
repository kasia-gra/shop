package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.CartDao;
import com.codecool.shop.dao.dao.LineItemDao;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;


import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class CartDaoJdbc implements CartDao {

    private final DataSource dataSource;
    private final LineItemDao lineItemDao;

    public CartDaoJdbc(DataSource dataSource, LineItemDao lineItemDao) {
        this.dataSource = dataSource;
        this.lineItemDao = lineItemDao;
    }

    @Override
    public void addEmptyCart(Cart cart, int productId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO cart (total_price, cart_size) VALUES (?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, (int) cart.getLineItemsTotalPrice());
            st.setInt(2, cart.getSize());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            cart.setId(rs.getInt(1));
            int addedQuantity = 1;
            System.out.println("CREATED EMPTY CART WITH ID " + cart.getId());
            addItemToCart(cart, productId, addedQuantity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addItemToCart(Cart cart, int productId, int addedQuantity) {
        int cartId = cart.getId();
        try (Connection connection = dataSource.getConnection()) {
            lineItemDao.addProduct(cart, productId, addedQuantity);
            String sql = "UPDATE cart SET total_price = ?, cart_size = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lineItemDao.getTotalValueOfLinesInCart(cartId));
            statement.setInt(2, lineItemDao.getTotalNumberOfLinesInCart(cartId));
            statement.setInt(3, cartId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating cart with id: " + cartId, exception);
        }
    }


    @Override
    public void removeItemFromCart(int productId, Cart cart) {
        int cartId = cart.getId();
        try (Connection connection = dataSource.getConnection()) {
            lineItemDao.remove(productId, cart);
            String sql = "UPDATE cart SET total_price = ?, cart_size = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lineItemDao.getTotalValueOfLinesInCart(cartId));
            statement.setInt(2, lineItemDao.getTotalNumberOfLinesInCart(cartId));
            statement.setInt(3, cartId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating cart", exception);
        }
    }

    @Override
    public Cart find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM cart WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            List<LineItem> lineItems = lineItemDao.findLineItemsByCartId(id);
            Cart cart = new Cart(resultSet.getInt("total_price"), resultSet.getInt("cart_size"),
                    lineItems);
            cart.setId(id);
            return cart;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving cart with id: " + id, exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE from cart WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,    id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
