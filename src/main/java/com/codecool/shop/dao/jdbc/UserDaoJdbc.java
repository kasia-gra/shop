package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.AddressDetailDao;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.model.AddressDetail;
import com.codecool.shop.model.user.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private final DataSource dataSource;
    private final AddressDetailDao addressDetailDao;

    public UserDaoJdbc(DataSource dataSource, AddressDetailDao addressDetailDao) {
        this.dataSource = dataSource;
        this.addressDetailDao = addressDetailDao;
    }

    @Override
    public void add(User user) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO \"user\" (first_name, last_name, email, password, phone_number, user_address_details_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            addressDetailDao.add(user.getUserAddressDetail());

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4,user.getPassword());
            statement.setString(5, user.getPhone());
            statement.setInt(6, user.getUserAddressDetail().getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException("Error while adding new user.", exception);
        }
    }

    @Override
    public User find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"user\" WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String phoneNumber = resultSet.getString("phone_number");
            AddressDetail userAddressDetail = addressDetailDao.find(resultSet.getInt("user_address_details_id"));

            User user = new User(firstName, lastName, email, phoneNumber, userAddressDetail);
            user.setId(id);

            return user;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving user with id: " + id, exception);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE \"user\" SET first_name = ?, last_name = ?,email = ?, phone_number = ?," +
                    "user_address_details_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setInt(5, user.getUserAddressDetail().getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating user with id: " + user.getId(), exception);
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM \"user\" WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Error while deleting user with id: " + id, exception);
        }
    }

    @Override
    public List<User> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"user\"";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phone_number");
                AddressDetail userAddressDetail = addressDetailDao.find(resultSet.getInt("user_address_details_id"));

                User user = new User(firstName, lastName, email, phoneNumber, userAddressDetail);
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }
            return users;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving all users.", exception);
        }
    }

    @Override
    public User findUserByEmail(String eMail) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"user\" WHERE email = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            User user = createUserBasedOnQueryResult(resultSet);
            return user;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving user with e-mail: " + eMail, exception);
        }
    }

    @Override
    public User findUserByEmailAndPassword(String eMail, String password) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM \"user\" WHERE email = ? AND password = ? ";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, eMail);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            User user = createUserBasedOnQueryResult(resultSet);
            return user;
        } catch (SQLException exception) {
            throw new RuntimeException("Error while retrieving user with e-mail: " + eMail, exception);
        }
    }

    private User createUserBasedOnQueryResult(ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phone_number");
        AddressDetail userAddressDetail = addressDetailDao.find(resultSet.getInt("user_address_details_id"));
        User user = new User(firstName, lastName, email, phoneNumber, userAddressDetail);
        user.setId(resultSet.getInt("id"));
        return user;
    }

}
