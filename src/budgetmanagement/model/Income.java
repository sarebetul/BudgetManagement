package budgetmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income {
    private int id;
    private String name;
    private BigDecimal amount;
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Income(int id, String name, BigDecimal amount, LocalDate date){
        this.amount = amount;
        this.id = id;
        this.date = date;
        this.name = name;
    }
}
