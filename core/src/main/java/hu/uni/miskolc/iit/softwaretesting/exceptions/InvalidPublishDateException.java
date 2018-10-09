package hu.uni.miskolc.iit.softwaretesting.exceptions;

//invalid publish data i.e.: negative numbers

public class InvalidPublishDateException  extends Exception{

    public InvalidPublishDateException() {
    }

    public InvalidPublishDateException(String message) {
        super(message);
    }

    public InvalidPublishDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPublishDateException(Throwable cause) {
        super(cause);
    }

    public InvalidPublishDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
