package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class WrongISBNException  extends Exception{

    public WrongISBNException(String s) { super("The referred ISBN is wrong!"+s);}
}
