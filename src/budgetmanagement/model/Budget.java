package budgetmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Budget {
    private int id;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal remainingAmount;

    public Budget(int id, int userId, LocalDate startDate, LocalDate endDate,
                  BigDecimal totalExpense, BigDecimal totalIncome, BigDecimal remainingAmount) {
        this.id = id;
        this.userId = userId;
        this.endDate = endDate;
        this.remainingAmount = remainingAmount;
        this.startDate = startDate;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public Budget(int id, int userId, LocalDate startDate, LocalDate endDate) {
        this(id, userId, startDate, endDate, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}