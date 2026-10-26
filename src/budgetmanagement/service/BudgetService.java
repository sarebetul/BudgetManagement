package budgetmanagement.service;

import budgetmanagement.dao.BudgetDAO;
import budgetmanagement.dao.ExpenseDAO;
import budgetmanagement.dao.IncomeDAO;
import budgetmanagement.model.Budget;

import java.math.BigDecimal;
import java.util.List;

public class BudgetService {

    private final BudgetDAO budgetDAO = new BudgetDAO();
    private final IncomeDAO incomeDAO = new IncomeDAO();
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public Budget getBudgetWithTotals(int budgetId) {
        Budget budget = budgetDAO.getBudgetById(budgetId);
        if (budget == null) {
            return null;
        }
        populateTotals(budget);
        return budget;
    }

    public List<Budget> getBudgetsWithTotalsByUserId(int userId) {
        List<Budget> budgets = budgetDAO.getBudgetsByUserId(userId);
        for (Budget budget : budgets) {
            populateTotals(budget);
        }
        return budgets;
    }

    private void populateTotals(Budget budget) {
        BigDecimal totalIncome = incomeDAO.sumIncomeByUserAndDateRange(
                budget.getUserId(), budget.getStartDate(), budget.getEndDate());
        BigDecimal totalExpense = expenseDAO.sumExpenseByUserAndDateRange(
                budget.getUserId(), budget.getStartDate(), budget.getEndDate());

        budget.setTotalIncome(totalIncome);
        budget.setTotalExpense(totalExpense);
        budget.setRemainingAmount(totalIncome.subtract(totalExpense));
    }
}