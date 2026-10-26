package budgetmanagement.dao;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Notification;
import budgetmanagement.model.NotificationType;
import budgetmanagement.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void addNotification(Notification notification, int userId) {
        String sql = "INSERT INTO notifications (user_id, message, date, is_read, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, notification.getMessage());
            statement.setDate(3, java.sql.Date.valueOf(notification.getDate()));
            statement.setBoolean(4, notification.isRead());
            statement.setString(5, notification.getType().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Bildirim eklenirken bir hata oluştu.", e);
        }
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                notifications.add(new Notification(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getBoolean("is_read"),
                        NotificationType.valueOf(resultSet.getString("type"))
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Bildirimler listelenirken bir hata oluştu.", e);
        }
        return notifications;
    }

    public boolean markAsRead(int id) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Bildirim güncellenirken bir hata oluştu.", e);
        }
    }

    public boolean deleteNotification(int id) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Bildirim silinirken bir hata oluştu.", e);
        }
    }
}