package hu.uni.miskolc.iit.softwaretesting.exceptions;

/**
 * Represents that the borrowing is already exists in database.
 */
public class AlreadyExistingBorrowingException extends Exception {
    public AlreadyExistingBorrowingException() {
    }

    public AlreadyExistingBorrowingException(String message) {
        super(message);
    }

    public AlreadyExistingBorrowingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistingBorrowingException(Throwable cause) {
        super(cause);
    }

    public AlreadyExistingBorrowingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
