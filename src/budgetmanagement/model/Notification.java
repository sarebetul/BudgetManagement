package budgetmanagement.model;

import java.time.LocalDate;

public class Notification {
    private int id;
    private int userId;
    private String message;
    private LocalDate date;
    private boolean isRead;
    private NotificationType type;

    public Notification(int id, int userId, String message, LocalDate date, boolean isRead, NotificationType type) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
        this.type = type;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}