package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class EmptyFieldException  extends Exception{

    public EmptyFieldException(String s) { super("The field is empty"+s);}
}
