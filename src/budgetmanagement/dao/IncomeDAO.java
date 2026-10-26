package budgetmanagement.dao;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Income;
import budgetmanagement.util.DbConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    public void addIncome(Income income, int userId) {
        String sql = "INSERT INTO incomes (user_id, name, amount, date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, income.getName());
            statement.setBigDecimal(3, income.getAmount());
            statement.setDate(4, java.sql.Date.valueOf(income.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gelir eklenirken bir hata oluştu.", e);
        }
    }

    public Income getIncomeById(int id) {
        String sql = "SELECT * FROM incomes WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Income(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gelir aranırken bir hata oluştu.", e);
        }
        return null;
    }

    public List<Income> getIncomesByUserId(int userId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM incomes WHERE user_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                incomes.add(new Income(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gelirler listelenirken bir hata oluştu.", e);
        }
        return incomes;
    }

    public boolean updateIncome(Income income) {
        String sql = "UPDATE incomes SET name = ?, amount = ?, date = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, income.getName());
            statement.setBigDecimal(2, income.getAmount());
            statement.setDate(3, java.sql.Date.valueOf(income.getDate()));
            statement.setInt(4, income.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Gelir güncellenirken bir hata oluştu.", e);
        }
    }

    public boolean deleteIncome(int id) {
        String sql = "DELETE FROM incomes WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Gelir silinirken bir hata oluştu.", e);
        }
    }

    public BigDecimal sumIncomeByUserAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM incomes WHERE user_id = ? AND date BETWEEN ? AND ?";
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
            throw new DatabaseException("Gelir toplamı hesaplanırken bir hata oluştu.", e);
        }
        return BigDecimal.ZERO;
    }
}