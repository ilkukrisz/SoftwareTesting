package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class AlreadyExistingBookInstanceException extends Exception {

    public AlreadyExistingBookInstanceException() {
    }

    public AlreadyExistingBookInstanceException(String s) {
        super(s);
    }

    public AlreadyExistingBookInstanceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AlreadyExistingBookInstanceException(Throwable throwable) {
        super(throwable);
    }

    public AlreadyExistingBookInstanceException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
