package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.SessionDao;
import com.codecool.shop.model.Session;
import com.codecool.shop.model.user.Address;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDaoJdbc implements SessionDao {
    private final DataSource dataSource;

    public SessionDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Session session) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO session DEFAULT VALUES";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            session.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new session.", exception);
        }
    }

    @Override
    public Session find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM session WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Session session =  new Session();
            session.setId(id);
            return session;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving session with id: " + id, exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM session WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting session with id: " + id, exception);
        }
    }

    @Override
    public List<Session> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM session";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<Session> sessions = new ArrayList<>();
            while (resultSet.next()) {
                Session session = new Session();
                session.setId(resultSet.getInt("id"));
                sessions.add(session);
            }
            return sessions;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving all sessions.", exception);
        }
    }
}
