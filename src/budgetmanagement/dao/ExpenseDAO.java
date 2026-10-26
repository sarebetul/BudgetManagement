package budgetmanagement.dao;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Expense;
import budgetmanagement.util.DbConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpenseDAO {

    public void addExpense(Expense expense, int userId) {
        String sql = "INSERT INTO expenses (user_id, category_id, amount, description, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, expense.getCategoryId());
            statement.setBigDecimal(3, expense.getAmount());
            statement.setString(4, expense.getDescription());
            statement.setDate(5, java.sql.Date.valueOf(expense.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gider eklenirken bir hata oluştu.", e);
        }
    }

    public Expense getExpenseById(int id) {
        String sql = "SELECT * FROM expenses WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("category_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getString("description"),
                        resultSet.getDate("date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gider aranırken bir hata oluştu.", e);
        }
        return null;
    }

    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                expenses.add(new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("category_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getString("description"),
                        resultSet.getDate("date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Giderler listelenirken bir hata oluştu.", e);
        }
        return expenses;
    }

    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET category_id = ?, amount = ?, description = ?, date = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, expense.getCategoryId());
            statement.setBigDecimal(2, expense.getAmount());
            statement.setString(3, expense.getDescription());
            statement.setDate(4, java.sql.Date.valueOf(expense.getDate()));
            statement.setInt(5, expense.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Gider güncellenirken bir hata oluştu.", e);
        }
    }

    public boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Gider silinirken bir hata oluştu.", e);
        }
    }

    public BigDecimal sumExpenseByUserAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM expenses WHERE user_id = ? AND date BETWEEN ? AND ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(startDate));
            statement.setDate(3, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gider toplamı hesaplanırken bir hata oluştu.", e);
        }
        return BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> getCategoryTotalsByUserAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        Map<String, BigDecimal> categoryTotals = new LinkedHashMap<>();
        String sql = "SELECT c.name AS category_name, SUM(e.amount) AS total " +
                "FROM expenses e JOIN categories c ON e.category_id = c.id " +
                "WHERE e.user_id = ? AND e.date BETWEEN ? AND ? " +
                "GROUP BY c.name ORDER BY total DESC";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(startDate));
            statement.setDate(3, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categoryTotals.put(resultSet.getString("category_name"), resultSet.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kategori analizi hesaplanırken bir hata oluştu.", e);
        }
        return categoryTotals;
    }
}