package budgetmanagement.dao;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Debt;
import budgetmanagement.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DebtDAO {

    public void addDebt(Debt debt, int userId) {
        String sql = "INSERT INTO debts (user_id, name, total_amount, installment_amount, " +
                "installment_count, due_date, payment_date, paid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, debt.getName());
            statement.setBigDecimal(3, debt.getTotalAmount());
            statement.setBigDecimal(4, debt.getInstallmentAmount());
            statement.setInt(5, debt.getInstallmentCount());
            statement.setDate(6, java.sql.Date.valueOf(debt.getDueDate()));
            statement.setDate(7, debt.getPaymentDate() != null ? java.sql.Date.valueOf(debt.getPaymentDate()) : null);
            statement.setBoolean(8, debt.isPaid());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Borç eklenirken bir hata oluştu.", e);
        }
    }

    public Debt getDebtById(int id) {
        String sql = "SELECT * FROM debts WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Borç aranırken bir hata oluştu.", e);
        }
        return null;
    }

    public List<Debt> getDebtsByUserId(int userId) {
        List<Debt> debts = new ArrayList<>();
        String sql = "SELECT * FROM debts WHERE user_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                debts.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Borçlar listelenirken bir hata oluştu.", e);
        }
        return debts;
    }

    public boolean updateDebt(Debt debt) {
        String sql = "UPDATE debts SET name = ?, total_amount = ?, installment_amount = ?, " +
                "installment_count = ?, due_date = ?, payment_date = ?, paid = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, debt.getName());
            statement.setBigDecimal(2, debt.getTotalAmount());
            statement.setBigDecimal(3, debt.getInstallmentAmount());
            statement.setInt(4, debt.getInstallmentCount());
            statement.setDate(5, java.sql.Date.valueOf(debt.getDueDate()));
            statement.setDate(6, debt.getPaymentDate() != null ? java.sql.Date.valueOf(debt.getPaymentDate()) : null);
            statement.setBoolean(7, debt.isPaid());
            statement.setInt(8, debt.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Borç güncellenirken bir hata oluştu.", e);
        }
    }

    public boolean deleteDebt(int id) {
        String sql = "DELETE FROM debts WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Borç silinirken bir hata oluştu.", e);
        }
    }

    private Debt mapRow(ResultSet resultSet) throws SQLException {
        java.sql.Date paymentDate = resultSet.getDate("payment_date");
        return new Debt(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("total_amount"),
                resultSet.getBigDecimal("installment_amount"),
                resultSet.getInt("installment_count"),
                resultSet.getDate("due_date").toLocalDate(),
                paymentDate != null ? paymentDate.toLocalDate() : null,
                resultSet.getBoolean("paid")
        );
    }
}