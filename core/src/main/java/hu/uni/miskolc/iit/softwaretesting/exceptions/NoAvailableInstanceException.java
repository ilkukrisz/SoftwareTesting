package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class NoAvailableInstanceException extends Exception {

    public NoAvailableInstanceException() {
    }

    public NoAvailableInstanceException(String s) {
        super(s);
    }

    public NoAvailableInstanceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoAvailableInstanceException(Throwable throwable) {
        super(throwable);
    }

    public NoAvailableInstanceException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
