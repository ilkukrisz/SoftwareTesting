package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class AlreadyExistingBookException  extends Exception {
    public AlreadyExistingBookException(String s) { super("The referred book is already exist: "+s);}
}
