package hu.uni.miskolc.iit.softwaretesting.exceptions;

public class NotExistingGenreException  extends Exception{

    public NotExistingGenreException(String s) { super("This genre does not exist!"+s);}
}
