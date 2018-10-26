package hu.uni.miskolc.iit.softwaretesting.exceptions;

/**
 * Represents that there are no borrowing in database.
 */
public class NoBorrowingsFoundException extends Exception {
    public NoBorrowingsFoundException() {
    }

    public NoBorrowingsFoundException(String message) {
        super(message);
    }

    public NoBorrowingsFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoBorrowingsFoundException(Throwable cause) {
        super(cause);
    }

    public NoBorrowingsFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
