package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class NoAvailableInstance extends Exception {

    public NoAvailableInstance() {
    }

    public NoAvailableInstance(String s) {
        super(s);
    }

    public NoAvailableInstance(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoAvailableInstance(Throwable throwable) {
        super(throwable);
    }

    public NoAvailableInstance(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
