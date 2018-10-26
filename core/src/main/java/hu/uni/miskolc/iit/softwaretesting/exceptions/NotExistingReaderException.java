package hu.uni.miskolc.iit.softwaretesting.exceptions;

/**
 * Represents that the reader is not exists in database.
 */
public class NotExistingReaderException extends Exception {
    public NotExistingReaderException() {
    }

    public NotExistingReaderException(String message) {
        super(message);
    }

    public NotExistingReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingReaderException(Throwable cause) {
        super(cause);
    }

    public NotExistingReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
