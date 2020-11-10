package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.CartDao;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.product.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoJdbc implements CartDao {

    private DataSource dataSource;
    private List<Supplier> data = new ArrayList<>();

    public CartDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void add(Cart cart) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO cart (total_price, cart_size) VALUES (?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, (int) cart.getTotalPrice());
            st.setInt(2, cart.getSize());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            cart.setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cart find(int id) {
        return null;
    }

    @Override
    public void addItemToCart(Cart cart, int productPrice, int addedQuantity) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE cart SET total_price = total_price + ?, cart_size = cart_size + ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productPrice);
            statement.setInt(2, addedQuantity);
            statement.setInt(3, cart.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating cart with id: " + cart, exception);
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
