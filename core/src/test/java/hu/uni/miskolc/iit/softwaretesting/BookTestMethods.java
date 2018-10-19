package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.Genre;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BookTestMethods {


    private Book book;
    private long testISBN = (long)654573;
    private int testPublishDate = 2010;


    @Before
    public void setUp() {
        try {
            book = new Book("Krisz", "Title", (long)97236589, 2005, Genre.Crimi);
        } catch (InvalidPublishDateException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSetISBN() {
        book.setIsbn(testISBN);
        assertTrue((book.getIsbn()==654573));
    }

    @Test
    public void testGetISBN() {
        assertTrue((book.getIsbn()==97236589));
    }


    @Test
    public void testSetAuthor() {
        book.setAuthor("Alma");
        assertTrue((book.getAuthor().equals("Alma")));
    }

    @Test
    public void testGetAuthor() {
        assertTrue((book.getAuthor().equals("Krisz")));
    }

    @Test
    public void testSetTitle() {
        book.setTitle("Korte");
        assertTrue((book.getTitle().equals("Korte")));
    }

    @Test
    public void testGetTitle() {
        assertTrue((book.getTitle().equals("Title")));
    }

    @Test
    public void testSetPublishDate() throws InvalidPublishDateException {
        book.setPublishDate(testPublishDate);
        assertTrue((book.getPublishDate() == 2010));
    }

    @Test(expected = InvalidPublishDateException.class)
    public void testSetPublishDateWithBelowZeroValue() throws InvalidPublishDateException{
        book.setPublishDate(-200);
    }

    @Test(expected = InvalidPublishDateException.class)
    public void testSetPublishDateWithDateAfterToday() throws InvalidPublishDateException{
        book.setPublishDate(2081);
    }

    @Test
    public void testGetPublishDate() {
        assertTrue((book.getPublishDate() == 2005));
    }

    @Test
    public void testSetGenre() {
        book.setGenre(Genre.Horror);
        assertTrue((book.getGenre().equals(Genre.valueOf("Horror"))));
    }

    @Test
    public void testGetGenre() {
        assertTrue((book.getGenre().equals(Genre.valueOf("Crimi"))));
    }
}
