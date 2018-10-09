package hu.uni.miskolc.iit.softwaretesting.exceptions;

// if a book we want to add, already exists, this exception will be useful

public class AlreadyExistingBookException  extends Exception {

    public AlreadyExistingBookException() {
    }

    public AlreadyExistingBookException(String message) {
        super(message);
    }

    public AlreadyExistingBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistingBookException(Throwable cause) {
        super(cause);
    }

    public AlreadyExistingBookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
