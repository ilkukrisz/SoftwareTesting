package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

public class BorrowingTestMethods {
    private Borrowing borrowing;
    private Reader reader;
    private Book book;
    private BookInstance bookInstance;
    private Calendar calCreationDate = Calendar.getInstance();
    private Calendar calExpirationDate = Calendar.getInstance();
    private long borrowID = 98236;
    private long testborrowID = 2015632;

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

        calCreationDate.set(2017, Calendar.OCTOBER, 10);
        calExpirationDate.set(2017, Calendar.NOVEMBER, 9);

        borrowing = new Borrowing(borrowID, reader, calCreationDate.getTime(), calExpirationDate.getTime()
                , BorrowStatus.REQUESTED, bookInstance);
    }


    @Test
    public void testGetBorrowingID() {
        assertTrue(borrowing.getBorrowID() == borrowID);
    }

    @Test
    public void testSetBorrowingID() {
        borrowing.setBorrowID(2015632);
        assertTrue(borrowing.getBorrowID() == testborrowID);
    }

    @Test
    public void testGetReader() {
        assertTrue(borrowing.getReader().equals(new Reader("ilkukrisz", new Password("passwd"), "Ilku",
                "Krisztian", "apple@apple.com", "06123456789")));
    }




}
