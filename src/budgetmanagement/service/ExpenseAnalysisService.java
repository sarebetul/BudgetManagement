package budgetmanagement.service;

import budgetmanagement.dao.ExpenseDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ExpenseAnalysisService {

    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public Map<String, BigDecimal> getCategoryBreakdown(int userId, LocalDate startDate, LocalDate endDate) {
        return expenseDAO.getCategoryTotalsByUserAndDateRange(userId, startDate, endDate);
    }

    public String getTopSpendingCategory(int userId, LocalDate startDate, LocalDate endDate) {
        Map<String, BigDecimal> breakdown = getCategoryBreakdown(userId, startDate, endDate);
        if (breakdown.isEmpty()) {
            return null;
        }
        return breakdown.keySet().iterator().next();
    }
}