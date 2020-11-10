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
import java.util.ArrayList;
import java.util.List;

public class LineItemDaoJdbc implements LineItemDao {

    private final DataSource dataSource;
    private final ProductDao productDao;
    private final SupplierDao supplierDao;
    private final ProductCategoryDaoJdbc productCategoryDao;

    private List<LineItem> lineItems = new ArrayList<>();

    public LineItemDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
        this.supplierDao = new SupplierDaoJdbc(dataSource);
        this.productCategoryDao = new ProductCategoryDaoJdbc(dataSource);
        this.productDao = new ProductDaoJdbc(dataSource, supplierDao, productCategoryDao);
    }

    @Override
    public void add(int cartId, int productId) {
        try (Connection conn = dataSource.getConnection()) {
            Product product = productDao.find(productId);
            String sql = "INSERT INTO line_item (cart_id, product_id, quantity, total_line_price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, cartId);
            statement.setInt(2, product.getId());
            statement.setInt(3, 1);
            statement.setInt(4, (int) product.getDefaultPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();;
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
            LineItem lineItem = new LineItem(product, resultSet.getInt("quantity"),
                    resultSet.getInt("cart_id"));
            lineItem.setLineId(id);
            return lineItem;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving line item with id: " + id, exception);
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
    public void updateQty(int cartId, int productId, int addedQuantity) {
        try (Connection connection = dataSource.getConnection()) {
            Product product = productDao.find(productId);
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

    @Override
    public List<LineItem> findLineItemsByCartId(int cartId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM cart WHERE cart_id = ?";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                LineItem lineItem = new LineItem(productDao.find(rs.getInt("product_id")),
                        rs.getInt("quantity"), rs.getInt("total_line_price"));
                lineItem.setLineId(rs.getInt(1));
                lineItems.add(lineItem);
            }
            return lineItems;
        } catch (SQLException e) {
            throw new RuntimeException("ERROR while reading line items " + e.getMessage());
        }
    }

    @Override
    public int getTotalValueOfLinesInCart(int cartId){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT SUM(total_line_price) as total_price FROM line_item WHERE cart_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return 0;
            }
            return resultSet.getInt("total_price");
        } catch (SQLException e) {
            throw new RuntimeException("ERROR while reading line items " + e.getMessage());
        }
    }

    @Override
    public int getTotalNumberOfLinesInCart(int cartId){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT SUM(quantity) as cart_size FROM line_item WHERE cart_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return 0;
            }
            return resultSet.getInt("cart_size");
        } catch (SQLException e) {
            throw new RuntimeException("ERROR while reading line items " + e.getMessage());
        }
    }

}
