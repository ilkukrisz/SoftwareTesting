package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class NotExistingGenreException  extends Exception{

    public NotExistingGenreException() {
    }

    public NotExistingGenreException(String message) {
        super(message);
    }

    public NotExistingGenreException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingGenreException(Throwable cause) {
        super(cause);
    }

    public NotExistingGenreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
