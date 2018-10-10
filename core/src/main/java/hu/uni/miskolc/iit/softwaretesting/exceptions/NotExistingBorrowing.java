package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class NotExistingBorrowing extends Exception {

    public NotExistingBorrowing() {
    }

    public NotExistingBorrowing(String s) {
        super(s);
    }

    public NotExistingBorrowing(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NotExistingBorrowing(Throwable throwable) {
        super(throwable);
    }

    public NotExistingBorrowing(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
