package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class BookInstanceNotFoundException extends Exception {

    public BookInstanceNotFoundException() {
    }

    public BookInstanceNotFoundException(String s) {
        super(s);
    }

    public BookInstanceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BookInstanceNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public BookInstanceNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
