package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.Genre;
import static org.junit.Assert.*;

import org.junit.Test;


public class BookConstructorTest {

    private Book book;

    @Test
    public void testConstructorWithCorrectValues() throws InvalidPublishDateException {
        book = new Book("Krisz", "Title", (long)97236589, 2005, Genre.Crimi);

    }

    @Test(expected = InvalidPublishDateException.class)
    public void testConstructorWithDateAfterToday() throws InvalidPublishDateException {
        book = new Book("Krisz", "Title", (long)97236589, 2090, Genre.Crimi);
    }

    @Test(expected = InvalidPublishDateException.class)
    public void testConstructorWithDateBeforeZero() throws InvalidPublishDateException {
        book = new Book("Krisz", "Title", (long)97236589, -20, Genre.Crimi);
    }
}
