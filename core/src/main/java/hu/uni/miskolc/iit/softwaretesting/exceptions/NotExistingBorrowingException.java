package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class NotExistingBorrowingException extends Exception {

    public NotExistingBorrowingException() {
    }

    public NotExistingBorrowingException(String s) {
        super(s);
    }

    public NotExistingBorrowingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NotExistingBorrowingException(Throwable throwable) {
        super(throwable);
    }

    public NotExistingBorrowingException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
