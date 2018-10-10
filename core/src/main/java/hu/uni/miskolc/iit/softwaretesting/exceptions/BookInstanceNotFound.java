package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class BookInstanceNotFound extends Exception {

    public BookInstanceNotFound() {
    }

    public BookInstanceNotFound(String s) {
        super(s);
    }

    public BookInstanceNotFound(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BookInstanceNotFound(Throwable throwable) {
        super(throwable);
    }

    public BookInstanceNotFound(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
