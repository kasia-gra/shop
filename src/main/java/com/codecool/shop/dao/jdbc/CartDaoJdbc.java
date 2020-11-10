package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.CartDao;
import com.codecool.shop.dao.dao.LineItemDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.SupplierDao;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.Supplier;

import javax.sound.sampled.Line;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoJdbc implements CartDao {

    private DataSource dataSource;
    private LineItemDao lineItemDao;
    private List<Supplier> data = new ArrayList<>();
    private Cart cart;
    private Product product;
    private final ProductDao productDao;
    private final SupplierDao supplierDao;
    private final ProductCategoryDaoJdbc productCategoryDao;

    public CartDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
        this.lineItemDao = new LineItemDaoJdbc( dataSource);
        this.supplierDao = new SupplierDaoJdbc(dataSource);
        this.productCategoryDao = new ProductCategoryDaoJdbc(dataSource);
        this.productDao = new ProductDaoJdbc(dataSource, supplierDao, productCategoryDao);
    }

    @Override
    public void add(Cart cart, int productId) {
        try (Connection conn = dataSource.getConnection()) {
            lineItemDao.add(cart.getId(), productId);
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
        try (Connection conn = dataSource.getConnection()) {
            LineItem lineItem = lineItemDao.find(id);
            String sql = "SELECT * FROM cart WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                return null;
            }
            List<LineItem> lineItems = lineItemDao.findLineItemsByCartId(id);
            return new Cart(resultSet.getInt("total_line_price"), resultSet.getInt("quantity"),
                    lineItems);
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving address with id: " + id, exception);
        }
    }

    @Override
    public void addItemToCart(int cartId, int productId, int addedQuantity) {
        try (Connection connection = dataSource.getConnection()) {
            lineItemDao.add(cartId, productId);
            String sql = "UPDATE cart SET total_price = ?, cart_size = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lineItemDao.getTotalValueOfLinesInCart(cartId));
            statement.setInt(2, lineItemDao.getTotalNumberOfLinesInCart(cartId));
            statement.setInt(3, cartId);
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
