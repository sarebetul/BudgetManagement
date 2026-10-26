package budgetmanagement.model;

import java.math.BigDecimal;

public class InvestmentScenario {

    private int id;
    private int userId;

    private BigDecimal availableAmount;

    private BigDecimal goldRatio;
    private BigDecimal usdRatio;
    private BigDecimal euroRatio;

    private BigDecimal goldAmount;
    private BigDecimal usdAmount;
    private BigDecimal euroAmount;

    public InvestmentScenario(
            int id,
            int userId,
            BigDecimal availableAmount,
            BigDecimal goldRatio,
            BigDecimal usdRatio,
            BigDecimal euroRatio,
            BigDecimal goldAmount,
            BigDecimal usdAmount,
            BigDecimal euroAmount) {

        this.id = id;
        this.userId = userId;
        this.availableAmount = availableAmount;
        this.goldRatio = goldRatio;
        this.usdRatio = usdRatio;
        this.euroRatio = euroRatio;
        this.goldAmount = goldAmount;
        this.usdAmount = usdAmount;
        this.euroAmount = euroAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getGoldRatio() {
        return goldRatio;
    }

    public void setGoldRatio(BigDecimal goldRatio) {
        this.goldRatio = goldRatio;
    }

    public BigDecimal getUsdRatio() {
        return usdRatio;
    }

    public void setUsdRatio(BigDecimal usdRatio) {
        this.usdRatio = usdRatio;
    }

    public BigDecimal getEuroRatio() {
        return euroRatio;
    }

    public void setEuroRatio(BigDecimal euroRatio) {
        this.euroRatio = euroRatio;
    }

    public BigDecimal getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(BigDecimal goldAmount) {
        this.goldAmount = goldAmount;
    }

    public BigDecimal getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(BigDecimal usdAmount) {
        this.usdAmount = usdAmount;
    }

    public BigDecimal getEuroAmount() {
        return euroAmount;
    }

    public void setEuroAmount(BigDecimal euroAmount) {
        this.euroAmount = euroAmount;
    }
}