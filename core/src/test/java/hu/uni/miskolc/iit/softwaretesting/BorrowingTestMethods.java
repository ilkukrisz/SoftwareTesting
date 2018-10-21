package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidArgumentException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.DynAnyPackage.Invalid;

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
    private Password password = new Password("passwd");
    @Before
    public void setUp() {
        reader = new Reader("ilkukrisz", password, "Ilku",
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
    public void testGetReader() {
        Reader testReader =  new Reader("ilkukrisz", password, "Ilku",
                "Krisztian", "apple@apple.com", "06123456789");
        assertTrue(borrowing.getReader().equals(testReader));
    }

    @Test
    public void testGetCreationDate() {
        assertTrue(borrowing.getCreationDate().toString().equalsIgnoreCase(calCreationDate.getTime().toString()));
    }

    @Test
    public void testGetExperitaionDate() {
        assertTrue(borrowing.getExpirationDate().toString().equalsIgnoreCase(calExpirationDate.getTime().toString()));
    }

    @Test
    public void testGetStatus() {
        assertTrue(borrowing.getStatus().equals(BorrowStatus.valueOf("REQUESTED")));
    }

    @Test
    public void testGetBookInstance() {
        assertTrue(borrowing.getBookInstance().equals(bookInstance));
    }

    @Test
    public void testSetBorrowingID() {
        borrowing.setBorrowID(2015632);
        assertTrue(borrowing.getBorrowID() == testborrowID);
    }

    @Test
    public void testSetReader() {
        Reader testReader =  new Reader("ilkukrisz", new Password("passwd"), "Ilku",
                "Krisztian", "apple@apple.com", "06123456789");
        borrowing.setReader(testReader);
        assertTrue(testReader.equals(borrowing.getReader()));
    }

    @Test
    public void testSetCreationDate() {
        calCreationDate.set(2017, Calendar.OCTOBER, 20);
        borrowing.setCreationDate(calCreationDate.getTime());
        assertTrue(borrowing.getCreationDate().toString().equalsIgnoreCase(calCreationDate.getTime().toString()));
    }
    @Test
    public void testSetExpirationDate() {
        calExpirationDate.set(2017, Calendar.NOVEMBER, 20);
        borrowing.setExpirationDate(calExpirationDate.getTime());
        assertTrue(borrowing.getExpirationDate().toString().equalsIgnoreCase(calExpirationDate.getTime().toString()));
    }
    @Test
    public void testSetStatus() {
        borrowing.setStatus(BorrowStatus.BORROWED);
        assertTrue(borrowing.getStatus().equals(BorrowStatus.valueOf("BORROWED")));
    }

    @Test
    public void testSetBookInstance() {
        BookInstance testBookInstance = new BookInstance((long)1798146, book, true);
        borrowing.setBookInstance(testBookInstance);
        assertTrue(borrowing.getBookInstance().equals(testBookInstance));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testSetCreationDateWithWrongValues() {
        calCreationDate.set(2040,Calendar.NOVEMBER, 10);
        borrowing.setCreationDate(calCreationDate.getTime());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testSetCreationDateWithNegativeValues() {
        calCreationDate.set(-1,Calendar.NOVEMBER, 10);
        borrowing.setCreationDate(calCreationDate.getTime());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testSetExpirationDateWithWrongValues() {
        calExpirationDate.set(2040,Calendar.NOVEMBER, 10);
        borrowing.setExpirationDate(calExpirationDate.getTime());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testSetExpirationDateWithNegativeValues() {
        calExpirationDate.set(-1,Calendar.NOVEMBER, 10);
        borrowing.setExpirationDate(calExpirationDate.getTime());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testSetExpirationDateWithLowerDatesThanCreation() {
        calExpirationDate.set(2015,Calendar.NOVEMBER, 10);
        borrowing.setExpirationDate(calExpirationDate.getTime());
    }


}
