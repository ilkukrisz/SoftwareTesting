package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.model.Reader;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BookDAOTest {

    private BookDaoXMLImpl dao;

    private String databaseLocation;

    private Book testBook;

    private Book book1;

    private Book book2;


    @Before
    public void setUp () throws IOException, SAXException, ParserConfigurationException, InvalidPublishDateException {
        //initialize test data fields
        this.databaseLocation = "src/test/resources/actualDatabase.xml";
        this.testBook = new Book("Alma", "Barack", (long) 102410,2008, Genre.valueOf("Crimi"));
        this.book1 = new Book("Pasztor Lajos", "Utazas a Fold korul", 123456, 2010, Genre.Travel);
        this.book2 = new Book("Nagy Kalman", "Analizis III.", 1234567, 2013, Genre.Scifi);
        //initialize unit with clean database
        String originalDatabasePath = "src/test/resources/originalDatabase.xml";
        this.copyFile(originalDatabasePath, this.databaseLocation, true);
        this.dao = new BookDaoXMLImpl(this.databaseLocation, this.databaseLocation);
    }

    @Test
    public void testBookDAOConstructor () throws IOException, SAXException, ParserConfigurationException {
        new BookDaoXMLImpl(databaseLocation, databaseLocation);
    }

    @Test
    public void testCreateBook () throws AlreadyExistingBookException, PersistenceException, BookNotFoundException {
        Book expected = this.testBook;
        dao.createBook(expected);

        Book actual = dao.getBookByISBN(102410);

        assertEquals(expected, actual);
    }

    @Test(expected = AlreadyExistingBookException.class)
    public void testCreateExistingBook () throws AlreadyExistingBookException, PersistenceException {
        dao.createBook(this.testBook);
        dao.createBook(this.testBook);
    }


    //TODO ...

    @Test
    public void testGetAllBooks() throws BookNotFoundException {
        List<Book> books = dao.getAllBooks();
        assertThat(books, not(empty()));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetAllBooksWithEmptyDatabase() throws BookNotFoundException, PersistenceException {
        dao.deleteBook(book1);
        dao.deleteBook(book2);
        dao.getAllBooks();
    }



    @Test
    public void testAvailableBooks() throws BookNotFoundException {
        List<Book> books = dao.getAvailableBooks();
        assertThat(books, not(empty()));
    }


    @Test(expected = BookNotFoundException.class)
    public void testGetAvailabeBooksWithEmptyDatabase() throws BookNotFoundException, PersistenceException {
        dao.deleteBook(book1);
        dao.deleteBook(book2);
        dao.getAvailableBooks();
    }


    @Test
    public void testgetBooksByTitle() throws BookNotFoundException {
        List<Book> books = dao.getBooksByTitle("Utazas a Fold korul");
        assertThat(books, not(empty()));
    }

    @Test(expected = BookNotFoundException.class)
    public void testgetBooksByTitleWithNotFoundTitle() throws BookNotFoundException {
        dao.getBooksByTitle("awegjlhawelghaweg");
    }

    @Test
    public void testGetBooksbyISBN() throws BookNotFoundException {
        Book book = dao.getBookByISBN(123456);
        assertThat(book, is(book1));
    }


    @Test(expected = BookNotFoundException.class)
    public void testGetBooksbyISBNWithNotExistingISBN() throws BookNotFoundException {
        dao.getBookByISBN(986556432);
    }


    @Test
    public void testGetBooksByAuthor() throws BookNotFoundException {
        List<Book> books = dao.getBooksByAuthor("Nagy Kalman");
        assertThat(books, not(empty()));
    }


    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByAuthorWithNotExistingAuthor() throws BookNotFoundException {
        dao.getBooksByAuthor("alghaléghélbh");

    }

    @Test
    public void testUpdateBook() throws BookNotFoundException, PersistenceException, InvalidPublishDateException {
        this.book1.setPublishDate(testBook.getPublishDate());
        this.book1.setAuthor(testBook.getAuthor());
        this.book1.setGenre(testBook.getGenre());
        this.book1.setTitle(testBook.getTitle());

        dao.updateBook(book1);

        assertEquals(book1.getTitle(), testBook.getTitle());
        assertEquals(book1.getAuthor(), testBook.getAuthor());
        assertEquals(book1.getPublishDate(), testBook.getPublishDate());
        assertEquals(book1.getGenre(), testBook.getGenre());
    }

    @Test
    public void testDeleteBook() throws BookNotFoundException, PersistenceException {
        dao.deleteBook(book1);
        List<Book> books = dao.getAllBooks();

        assertThat(books, not(contains(book1)));
    }

    @Test
    public void testGetBookByGenre() throws BookNotFoundException {
        assertThat(dao.getBooksByGenre(this.book2.getGenre()), contains(book2));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByGenreWithNotExistingBook() throws BookNotFoundException {
        dao.getBooksByGenre(Genre.History);
    }


    @Test
    public void testGetBooksByYear() throws BookNotFoundException {
        assertThat(dao.getBooksByYear(this.book2.getPublishDate()), contains(book2));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByYearWithWrongYear() throws BookNotFoundException {
        dao.getBooksByYear(2030);
    }
    private void copyFile(String from, String to, Boolean overwrite) {

        try {
            File fromFile = new File(from);
            File toFile = new File(to);

            if (!fromFile.exists()) {
                throw new IOException("File not found: " + from);
            }
            if (!fromFile.isFile()) {
                throw new IOException("Can't copy directories: " + from);
            }
            if (!fromFile.canRead()) {
                throw new IOException("Can't read file: " + from);
            }

            if (toFile.isDirectory()) {
                toFile = new File(toFile, fromFile.getName());
            }

            if (toFile.exists() && !overwrite) {
                throw new IOException("File already exists.");
            } else {
                String parent = toFile.getParent();
                if (parent == null) {
                    parent = System.getProperty("user.dir");
                }
                File dir = new File(parent);
                if (!dir.exists()) {
                    throw new IOException("Destination directory does not exist: " + parent);
                }
                if (dir.isFile()) {
                    throw new IOException("Destination is not a valid directory: " + parent);
                }
                if (!dir.canWrite()) {
                    throw new IOException("Can't write on destination: " + parent);
                }
            }

            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {

                fis = new FileInputStream(fromFile);
                fos = new FileOutputStream(toFile);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

            } finally {
                if (from != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
                if (to != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Problems when copying file.");
        }
    }



}