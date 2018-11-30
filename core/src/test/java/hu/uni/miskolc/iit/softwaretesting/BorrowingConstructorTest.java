package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidArgumentException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class BorrowingConstructorTest {

    private Borrowing borrowing;
    private Reader reader;
    private Book book;
    private BookInstance bookInstance;
    private Calendar calCreationDate = Calendar.getInstance();
    private Calendar calExpirationDate = Calendar.getInstance();

    @Before
    public void setUp() {
        reader = new Reader("ilkukrisz", new Password("passwd"), "Ilku",
                "Krisztian", "apple@apple.com", "06123456789");

        {
            try {
                book = new Book("Krisz", "Title", (long)97236589, 2005, Genre.Crimi);
            } catch (InvalidPublishDateException e) {
                e.printStackTrace();
            }
        }

        bookInstance = new BookInstance((long) 2135510, book, false);
    }


    @Test
    public void testConstructorWithLegalValues() {
        calCreationDate.set(2017, Calendar.OCTOBER, 10);
        calExpirationDate.set(2017, Calendar.NOVEMBER, 9);
        borrowing = new Borrowing((long) 98236, reader, calCreationDate.getTime(), calExpirationDate.getTime()
                , BorrowStatus.REQUESTED, bookInstance);
    }


    @Test(expected = InvalidArgumentException.class)
    public void testConstructorWithWrongCreationDateValues() {
        calCreationDate.set(2020, 11, 10);
        calExpirationDate.set(2017, 10, 10);
        borrowing = new Borrowing((long) 98236, reader, calCreationDate.getTime(),
                calExpirationDate.getTime(),BorrowStatus.REQUESTED, bookInstance);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorWithWrongExpirationDate() {
        calCreationDate.set(2010, 11, 10);
        calExpirationDate.set(2040, 10, 10);
        borrowing = new Borrowing((long) 98236, reader, calCreationDate.getTime(),
                calExpirationDate.getTime(),BorrowStatus.REQUESTED, bookInstance);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorWithLaterCreationDate() {
        calCreationDate.set(2017, 11, 10);
        calExpirationDate.set(2017, 10, 10);
        borrowing = new Borrowing((long) 98236, reader, calCreationDate.getTime(),
                calExpirationDate.getTime(),BorrowStatus.REQUESTED, bookInstance);
    }
}
