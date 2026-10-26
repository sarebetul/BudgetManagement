package budgetmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Debt {
    private int id;
    private int userId;
    private String name;
    private BigDecimal totalAmount;
    private BigDecimal installmentAmount;
    private int installmentCount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean paid;

    public Debt(int id, int userId, String name, BigDecimal totalAmount, BigDecimal installmentAmount,
                int installmentCount, LocalDate dueDate, LocalDate paymentDate, boolean paid) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.totalAmount = totalAmount;
        this.installmentAmount = installmentAmount;
        this.installmentCount = installmentCount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.paid = paid;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public int getInstallmentCount() { return installmentCount; }
    public void setInstallmentCount(int installmentCount) { this.installmentCount = installmentCount; }

    public BigDecimal getInstallmentAmount() { return installmentAmount; }
    public void setInstallmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}