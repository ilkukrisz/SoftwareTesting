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

    private BookInstance testBookinstance;

    private Book book1;

    private Book book2;

    private BookInstance dbInstance1;

    private BookInstance dbInstance2;

    private BookInstance dbInstance3;

    private hu.uni.miskolc.iit.softwaretesting.model.Reader dbReader;

    private hu.uni.miskolc.iit.softwaretesting.model.Reader testReader;

    private Borrowing dbBorrowing1;

    private Borrowing dbBorrowing2;

    private Borrowing dbBorrowing3;

    private Borrowing testBorrowing;

    @Before
    public void setUp () throws IOException, SAXException, ParserConfigurationException, InvalidPublishDateException {
        //initialize test data fields
        this.databaseLocation = "src/test/resources/actualDatabase.xml";
        this.testBook = new Book("Alma", "Barack", (long) 102410,2008, Genre.valueOf("Crimi"));
        this.book1 = new Book("Pasztor Lajos", "Utazas a Fold korul", 123456, 2010, Genre.Travel);
        this.book2 = new Book("Nagy Kalman", "Analizis III.", 1234567, 2013, Genre.Scifi);
        this.dbInstance1 = new BookInstance(123456789, book1, false);
        this.dbInstance2 = new BookInstance(123456790, book1, true);
        this.dbInstance3 = new BookInstance(123456791, book2, false);
        this.testBookinstance = new BookInstance(1111111111, book2, true);
        this.dbReader = new hu.uni.miskolc.iit.softwaretesting.model.Reader("feri", new Password("feri"),
                "Ferenc", "Kovacs", "kovfer@example.com", "0680123456");

        this.testReader = new Reader("ilkukrisz", new Password("asd"), "Ilku", "Krisztian",
                "alma@korte.szilva", "06304256194");
        Calendar creationDate = Calendar.getInstance();
        Calendar expirationDate = Calendar.getInstance();
        creationDate.set(2017, Calendar.JANUARY, 10);
        expirationDate.set(2017, Calendar.FEBRUARY, 26);
        this.dbBorrowing1 = new Borrowing(1234567, dbReader, creationDate.getTime(), expirationDate.getTime(), BorrowStatus.EXPIRED, dbInstance2);

        creationDate.set(2018, Calendar.NOVEMBER, 6);
        expirationDate.set(2018, Calendar.NOVEMBER, 12);
        this.dbBorrowing2 = new Borrowing(12345678, dbReader, creationDate.getTime(), expirationDate.getTime(), BorrowStatus.BORROWED, dbInstance2);

        creationDate.set(2018, Calendar.NOVEMBER, 6);
        expirationDate.set(2018, Calendar.NOVEMBER, 10);
        this.dbBorrowing3 = new Borrowing(22223456, dbReader, creationDate.getTime(), expirationDate.getTime(), BorrowStatus.REQUESTED, dbInstance3);

        creationDate.set(2018, Calendar.OCTOBER, 10);
        expirationDate.set(2018, Calendar.OCTOBER, 20);
        this.testBorrowing = new Borrowing(11111111, dbReader, creationDate.getTime(), expirationDate.getTime(), BorrowStatus.RETURNED, dbInstance1);



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
    public void testCreateBookInstance() throws BookNotFoundException, PersistenceException, AlreadyExistingBookInstanceException, BookInstanceNotFoundException {
        BookInstance expected = this.testBookinstance;
        dao.createBookInstance(this.testBookinstance);
        BookInstance actual = dao.getBookInstanceByInventoryNumber(1111111111);
        assertEquals(expected, actual);
    }

    @Test(expected = AlreadyExistingBookInstanceException.class)
    public void testCreateBookInstanceWithExistingBookInstance() throws BookNotFoundException, PersistenceException, AlreadyExistingBookInstanceException, BookInstanceNotFoundException {

        dao.createBookInstance(this.testBookinstance);
        dao.createBookInstance(this.testBookinstance);

    }

    @Test
    public void testGetAllBookinstances() throws BookInstanceNotFoundException {
        Collection<BookInstance> bookInstances = dao.getAllBookInstances();
        assertThat(bookInstances, not(empty()));
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testGetAllBookinstancesWithEmptyDatabase() throws BookInstanceNotFoundException, BookNotFoundException, PersistenceException {
        dao.deleteBook(book1);
        dao.deleteBook(book2);
        dao.getAllBookInstances();
    }

    @Test
    public void testGetAllInstancesOfBook() throws BookNotFoundException, PersistenceException, AlreadyExistingBookInstanceException, BookInstanceNotFoundException {
        Collection<BookInstance> actual = dao.getAllInstancesOfBook(book1);
        Collection<BookInstance> expected = new ArrayList<>();
        expected.add(this.dbInstance1);
        expected.add(this.dbInstance2);

        assertThat(actual.size(), is(2));
        assertEquals(expected, actual);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testGetAllInstancesOfBookWithNotExistingBook() throws BookNotFoundException, BookInstanceNotFoundException {
        dao.getAllInstancesOfBook(testBook);
    }


    @Test(expected = BookInstanceNotFoundException.class)
    public void testGetAllInstancesOfBookWithNoInstances() throws BookNotFoundException, BookInstanceNotFoundException {
        dao.deleteBookInstance(dbInstance1);
        dao.deleteBookInstance(dbInstance2);
        dao.deleteBookInstance(dbInstance3);
        dao.getAllInstancesOfBook(book1);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testAllInstancesOfBookForNotFound() throws BookNotFoundException, BookInstanceNotFoundException {
        dao.getAllInstancesOfBook(testBook);
    }


    @Test
    public void testAvailableInstancesOfBook() throws BookNotFoundException, BookInstanceNotFoundException {
        Collection<BookInstance> actual = dao.getAvailableInstancesOfBook(book1);
        Collection<BookInstance> expected = new ArrayList<>();
        expected.add(this.dbInstance1);

        assertThat(actual.size(), is(1));
        assertEquals(expected, actual);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testAvailableInstancesOfBookForNotFound() throws BookNotFoundException, BookInstanceNotFoundException {
        dao.getAvailableInstancesOfBook(testBook);
    }


    @Test
    public void testupdateBookInstance() throws PersistenceException, BookInstanceNotFoundException, BookNotFoundException {
        this.dbInstance1.setBook(testBookinstance.getBook());
        this.dbInstance1.setLoaned(testBookinstance.isLoaned());

        dao.updateBookInstance(dbInstance1);

        BookInstance actual = dao.getBookInstanceByInventoryNumber(dbInstance1.getInventoryNumber());
        BookInstance expected = this.testBookinstance;

        assertEquals(actual.getBook(), expected.getBook());
        assertEquals(actual.isLoaned(), expected.isLoaned());
    }

    @Test
    public void testDeleteBookinstance() throws BookInstanceNotFoundException {
        dao.deleteBookInstance(dbInstance1);
        List<BookInstance> bookInstances = dao.getAllBookInstances();

        assertThat(bookInstances, not(contains(dbInstance1)));
    }


    @Test
    public void testCreateBorrowing() throws AlreadyExistingBorrowingException, PersistenceException, NotExistingBorrowingException {
        Borrowing expected = this.testBorrowing;
        dao.createBorrowing(this.testBorrowing);

        Borrowing actual = dao.getBorrowingById(this.testBorrowing.getBorrowID());

        assertEquals(expected, actual);
    }

    @Test(expected = AlreadyExistingBorrowingException.class)
    public void testCreateBorrowingWithAlreadyExistingBorrowing() throws AlreadyExistingBorrowingException, PersistenceException {
        dao.createBorrowing(testBorrowing);
        dao.createBorrowing(testBorrowing);
    }


    @Test
    public void testGetAllBorrowings() throws NoBorrowingsFoundException {
        assertThat(dao.getAllBorrowings(), not(empty()));
    }

    @Test(expected = NoBorrowingsFoundException.class)
    public void testGetAllBorrowingsWithNotExistingBookinstance() throws NoBorrowingsFoundException, BookInstanceNotFoundException {
        dao.deleteBookInstance(dbInstance1);
        dao.deleteBookInstance(dbInstance2);
        dao.deleteBookInstance(dbInstance3);
        dao.getAllBorrowings();
    }

    @Test(expected = NoBorrowingsFoundException.class)
    public void testGetAllBorrowingsWithNotExistingBook() throws NoBorrowingsFoundException, BookNotFoundException, PersistenceException {
        dao.deleteBook(book1);
        dao.deleteBook(book2);
        dao.getAllBorrowings();
    }

    @Test
    public void testGetBorrowingsByStatus() throws NotExistingBorrowingException {
        Borrowing expected = this.dbBorrowing3;
        Collection<Borrowing> actual = dao.getBorrowingsByStatus(BorrowStatus.REQUESTED);

        assertThat(actual, contains(expected));
    }


    @Test
    public void testBorrowingsOfReader() throws NotExistingReaderException, NotExistingBorrowingException {
        Collection<Borrowing> expected = new ArrayList<>();
        expected.add(this.dbBorrowing1);
        expected.add(this.dbBorrowing2);
        expected.add(this.dbBorrowing3);
        assertEquals(dao.getBorrowingsOfReader(dbReader), expected);
    }

    @Test(expected = NotExistingReaderException.class)
    public void testBorrowingsOfReaderWithNotExistingReader() throws NotExistingReaderException, NotExistingBorrowingException {
        dao.getBorrowingsOfReader(testReader);
    }

    @Test
    public void testGetBorrowingByID() throws NotExistingBorrowingException {
        Borrowing expected = this.dbBorrowing2;
        Borrowing actual = dao.getBorrowingById(dbBorrowing2.getBorrowID());
        assertEquals(actual, expected);
    }

    @Test(expected = NotExistingBorrowingException.class)
    public void testGetBorrowingByIDWithInvalidID() throws NotExistingBorrowingException {
        dao.getBorrowingById(53453453);
    }

    @Test
    public void testUpdateBorrowing() throws NotExistingBorrowingException, PersistenceException {
        this.dbBorrowing1.setStatus(testBorrowing.getStatus());
        this.dbBorrowing1.setReader(testBorrowing.getReader());

        Borrowing expected = this.testBorrowing;

        dao.updateBorrowing(this.dbBorrowing1);
        Borrowing actual = dao.getBorrowingById(dbBorrowing1.getBorrowID());

        assertEquals(actual.getReader(), expected.getReader());
        assertEquals(actual.getStatus(), expected.getStatus());

    }

    @Test
    public void testDeleteBorrowing() throws NotExistingBorrowingException, NoBorrowingsFoundException {
        dao.deleteBorrowing(dbBorrowing2);
        assertThat(dao.getAllBorrowings(), not(contains(dbBorrowing2)));
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