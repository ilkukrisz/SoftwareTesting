package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class WrongISBNException  extends Exception{

    public WrongISBNException() {
    }

    public WrongISBNException(String message) {
        super(message);
    }

    public WrongISBNException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongISBNException(Throwable cause) {
        super(cause);
    }

    public WrongISBNException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
