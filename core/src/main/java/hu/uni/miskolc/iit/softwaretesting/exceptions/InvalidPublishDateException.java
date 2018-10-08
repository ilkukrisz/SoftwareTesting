package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class InvalidPublishDateException  extends Exception{

    public InvalidPublishDateException(String s) { super("The publish date is invalid!"+s);}
}
