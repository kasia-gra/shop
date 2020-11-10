package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.LineItemDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.SupplierDao;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;
import com.codecool.shop.model.user.Address;
import jdk.jfr.Category;

import javax.sql.DataSource;
import java.sql.*;

public class LineItemDaoJdbc implements LineItemDao {

    private final DataSource dataSource;
    private final ProductDao productDao;
    private final SupplierDao supplierDao;
    private final ProductCategoryDaoJdbc productCategoryDao;

    public LineItemDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
        this.supplierDao = new SupplierDaoJdbc(dataSource);
        this.productCategoryDao = new ProductCategoryDaoJdbc(dataSource);
        this.productDao = new ProductDaoJdbc(dataSource, supplierDao, productCategoryDao);
    }

    @Override
    public void add(LineItem lineItem) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO line_item (cart_id, product_id, quantity, total_line_price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, lineItem.getCartId());
            statement.setInt(2, lineItem.getProduct().getId());
            statement.setInt(3, lineItem.getQty());
            statement.setInt(4, (int) lineItem.getLinePrice());
            statement.executeUpdate();
// TODO: 10/11/2020 add item to cart -> update cart
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            lineItem.setLineId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new line item.", exception);
        }
    }

    @Override
    public LineItem find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM line_item WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                return null;
            }
            Product product = productDao.find(id);
            return new LineItem(product, resultSet.getInt("quantity"), resultSet.getInt("id"),
                    resultSet.getInt("cart_id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving address with id: " + id, exception);
        }

    }

    // TODO: 10/11/2020 This function  maybe replaced with updateQty - tbd with the Team
    @Override
    public void update(int cartId, Product product) {

    }

    // TODO: 10/11/2020 This function  maybe replaced with updateQty - tbd with the Team
    @Override
    public void addOneItem(int cartId, Product product) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE line_item SET quantity = quantity + 1, total_line_price = total_line_price + 1 " +
                    "WHERE cart_id = ? AND product_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cartId);
            statement.setInt(2, product.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating line item", exception);
        }
    }

    @Override
    public void updateQty(int cartId, Product product, int addedQuantity) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE line_item SET quantity = quantity + ?, total_line_price = total_line_price + ? " +
                    "WHERE cart_id = ? AND product_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, addedQuantity);
            statement.setInt(2, (int) (product.getDefaultPrice() * addedQuantity));
            statement.setInt(3, cartId);
            statement.setInt(4, product.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating line item", exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM line_item WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting line_item with id: " + id, exception);
        }

    }
}
