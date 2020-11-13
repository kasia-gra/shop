package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.LogfileDao;
import com.codecool.shop.model.Logfile;

import javax.sql.DataSource;
import java.sql.*;

public class LogfileDaoJdbc implements LogfileDao {
    private final DataSource dataSource;

    public LogfileDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Logfile logfile) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO logfile (order_id, filename) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, logfile.getOrderId());
            statement.setString(2, logfile.getFilename());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            logfile.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new logfile.", exception);
        }
    }

    @Override
    public Logfile find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM logfile WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Logfile logfile = new Logfile(resultSet.getInt("order_id"), resultSet.getString("filename"));
            logfile.setId(id);
            return logfile;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving logfile with id: " + id, exception);
        }
    }

    @Override
    public Logfile getBy(int orderId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM logfile WHERE order_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Logfile logfile = new Logfile(resultSet.getInt("order_id"), resultSet.getString("filename"));
            logfile.setId(orderId);
            return logfile;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving logfile with orderId: " + orderId, exception);
        }
    }
}
