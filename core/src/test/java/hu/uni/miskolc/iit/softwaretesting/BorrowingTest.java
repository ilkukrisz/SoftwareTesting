package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidArgumentException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class BorrowingTest {

    private Borrowing borrowing;
    private Reader reader;
    private Book book;
    private BookInstance bookInstance;

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
        borrowing = new Borrowing((long) 98236, reader, new Date(2017,10,10),
                new Date(2017,11,9), BorrowStatus.REQUESTED, bookInstance);
    }


    @Test(expected = InvalidArgumentException.class)
    public void testConstructorWithWrongDateValues() {
        borrowing = new Borrowing((long) 98236, reader, new Date(2020,11,10),
                new Date(2017,10,10),BorrowStatus.REQUESTED, bookInstance);
    }

}
