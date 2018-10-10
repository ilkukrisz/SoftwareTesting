package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class AlreadyExistingBookInstance extends Exception {

    public AlreadyExistingBookInstance() {
    }

    public AlreadyExistingBookInstance(String s) {
        super(s);
    }

    public AlreadyExistingBookInstance(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AlreadyExistingBookInstance(Throwable throwable) {
        super(throwable);
    }

    public AlreadyExistingBookInstance(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
