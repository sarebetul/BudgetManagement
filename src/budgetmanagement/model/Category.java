package budgetmanagement.model;

public class Category {
    private int id;
    private int userId;
    private String name;
    private boolean isDefault;

    public Category(int id, int userId, String name, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.isDefault = isDefault;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return name;
    }
}