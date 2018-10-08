package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class BookNotFoundException extends Exception{

    public BookNotFoundException(String s) { super("The referred book does not exists: "+s);}
}
