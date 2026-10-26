package budgetmanagement.exception;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message, Throwable cause) {
        super(message + "\nSebep: " + (cause != null ? cause.toString() : "bilinmiyor"), cause);
    }
}