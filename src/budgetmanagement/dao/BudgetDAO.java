package budgetmanagement.dao;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Budget;
import budgetmanagement.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    public void addBudget(Budget budget, int userId) {
        String sql = "INSERT INTO budgets (user_id, start_date, end_date) VALUES (?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(budget.getStartDate()));
            statement.setDate(3, java.sql.Date.valueOf(budget.getEndDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Bütçe eklenirken bir hata oluştu.", e);
        }
    }

    public Budget getBudgetById(int id) {
        String sql = "SELECT * FROM budgets WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Budget(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Bütçe aranırken bir hata oluştu.", e);
        }
        return null;
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                budgets.add(new Budget(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Bütçeler listelenirken bir hata oluştu.", e);
        }
        return budgets;
    }

    public boolean updateBudget(Budget budget) {
        String sql = "UPDATE budgets SET start_date = ?, end_date = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(budget.getStartDate()));
            statement.setDate(2, java.sql.Date.valueOf(budget.getEndDate()));
            statement.setInt(3, budget.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Bütçe güncellenirken bir hata oluştu.", e);
        }
    }

    public boolean deleteBudget(int id) {
        String sql = "DELETE FROM budgets WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Bütçe silinirken bir hata oluştu.", e);
        }
    }
}