package budgetmanagement.dao;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.User;
import budgetmanagement.util.DbConnection;
import budgetmanagement.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, password_hash) VALUES (?,?,?,?)";
        try (Connection connection = DbConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, PasswordUtil.hashPassword(user.getPassword()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Kullanıcı eklenirken bir hata oluştu.", e);
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kullanıcı aranırken bir hata oluştu.", e);
        }
        return null;
    }

    public User authenticateUser(String email, String plainPassword) {
        User user = getUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (PasswordUtil.checkPassword(plainPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Kullanıcı güncellenirken bir hata oluştu.", e);
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Kullanıcı silinirken bir hata oluştu.", e);
        }
    }
}