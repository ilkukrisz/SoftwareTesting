package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.exceptions.EmptyFieldException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidArgumentException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;

import hu.uni.miskolc.iit.softwaretesting.model.Genre;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class BookInstanceTest{

    BookInstance bookInstance;
    long inventoryNumber =  120002;
    Book book;
    String author = "John Doe";
    String title = "Nothing";
    long isbn = 1012012;
    int publishDate = 1995;
    Genre genre = Genre.Horror;
    boolean isLoaned = true;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp () throws InvalidPublishDateException
    {

        this.book = new Book(this.author, this.title, this.isbn, this.publishDate, this.genre);
        this.bookInstance = new BookInstance(this.inventoryNumber,this.book,this.isLoaned);
    }

    @Test
    public void testConstructortWithLegalValues()
    {
        try
        {
            new BookInstance(this.inventoryNumber,this.book,this.isLoaned);
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }
    @Test
    public void testConstructorWithIllegalValues()
    {
        exceptionRule.expect(IllegalArgumentException.class);
        new BookInstance(0,this.book,this.isLoaned);
        exceptionRule.expect(IllegalArgumentException.class);
        new BookInstance(this.inventoryNumber,null,this.isLoaned);
        exceptionRule.expect(IllegalArgumentException.class);
        new BookInstance(this.inventoryNumber, this.book, false);
    }

    @Test
    public void testGetInventoryNumber ()
    {
        assertEquals(this.inventoryNumber, this.bookInstance.getInventoryNumber());
    }
    @Test(expected = InvalidArgumentException.class)
    public void testSetNegativeInventoryNumber()
    {
        long inventoryNumber = -1000;
        this.bookInstance.setInventoryNumber((inventoryNumber));
    }

    @Test
    public void testSetInventoryNumber()
    {
        long inventoryNumber = 12345;
        this.bookInstance.setInventoryNumber(inventoryNumber);
        assertEquals(this.inventoryNumber, this.bookInstance.getInventoryNumber());
    }
    @Test
    public void testGetBook()
    {
        assertEquals(this.book, this.bookInstance.getBook());
    }
    @Test
    public void testSetBook() throws InvalidPublishDateException
    {
        book = new Book("John Doe","ASD",110 ,1994, Genre.Science);
        this.bookInstance.setBook(book);
        assertEquals(book, this.bookInstance.getBook());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSetBookInstanceWithNoBook()
    {
       this.bookInstance.setBook(null);
    }
    @Test
    public void testSetLoaned()
    {
        boolean loaned = true;
        this.bookInstance.setLoaned(loaned);
        assertEquals(this.isLoaned, this.bookInstance.isLoaned());
    }
    @Test
    public void isLoaned()
    {
        assertEquals(this.isLoaned, this.bookInstance.isLoaned());
    }
}
