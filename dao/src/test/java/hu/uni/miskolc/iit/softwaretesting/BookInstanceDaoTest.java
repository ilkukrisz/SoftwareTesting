package hu.uni.miskolc.iit.softwaretesting;
import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.dao.BookInstanceDaoXMLImpl;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.model.Reader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BookInstanceDaoTest {

    private Book testBook;

    private BookInstance testBookinstance;

    @Mock
    private BookDaoXMLImpl bookDaoXML;

    @InjectMocks
    private BookInstanceDaoXMLImpl bookInstanceDao;
    
    private String databaseLocation;

    private Book book1;

    private Book book2;

    private BookInstance dbInstance1;

    private BookInstance dbInstance2;

    private BookInstance dbInstance3;

    private hu.uni.miskolc.iit.softwaretesting.model.Reader dbReader;


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

        //initialize unit with clean database
        String originalDatabasePath = "src/test/resources/originalDatabase.xml";
        this.copyFile(originalDatabasePath, this.databaseLocation, true);
        this.bookInstanceDao = new BookInstanceDaoXMLImpl(this.databaseLocation, this.databaseLocation);

        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testCreateBookInstance() throws BookNotFoundException, PersistenceException, AlreadyExistingBookInstanceException, BookInstanceNotFoundException {
        Mockito.doReturn(this.book2).when(bookDaoXML).getBookByISBN(1234567);

        BookInstance expected = this.testBookinstance;
        bookInstanceDao.createBookInstance(this.testBookinstance);
        BookInstance actual = bookInstanceDao.getBookInstanceByInventoryNumber(this.testBookinstance.getInventoryNumber());
        assertEquals(expected, actual);
    }

    @Test(expected = AlreadyExistingBookInstanceException.class)
    public void testCreateBookInstanceWithExistingBookInstance() throws BookNotFoundException, PersistenceException, AlreadyExistingBookInstanceException, BookInstanceNotFoundException {

        bookInstanceDao.createBookInstance(this.testBookinstance);
        bookInstanceDao.createBookInstance(this.testBookinstance);

    }

    @Test
    public void testGetAllBookinstances() throws BookInstanceNotFoundException {
        Collection<BookInstance> bookInstances = bookInstanceDao.getAllBookInstances();
        assertThat(bookInstances, not(empty()));
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testGetAllBookinstancesWithEmptyDatabase() throws BookInstanceNotFoundException, BookNotFoundException, PersistenceException {
        bookInstanceDao.deleteBookInstance(this.dbInstance1);
        bookInstanceDao.deleteBookInstance(this.dbInstance2);
        bookInstanceDao.deleteBookInstance(this.dbInstance3);

        Mockito.doReturn(this.book1).when(bookDaoXML).getBookByISBN(book1.getIsbn());
        Mockito.doReturn(this.book2).when(bookDaoXML).getBookByISBN(book2.getIsbn());

        bookInstanceDao.getAllBookInstances();
    }

    @Test
    public void testGetAllInstancesOfBook() throws BookNotFoundException, PersistenceException, AlreadyExistingBookInstanceException, BookInstanceNotFoundException {
        Mockito.doReturn(this.book1).when(bookDaoXML).getBookByISBN(book1.getIsbn());
        Collection<BookInstance> actual = bookInstanceDao.getAllInstancesOfBook(book1);
        Collection<BookInstance> expected = new ArrayList<>();
        expected.add(this.dbInstance1);
        expected.add(this.dbInstance2);

        assertThat(actual.size(), is(2));
        assertEquals(expected, actual);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testGetAllInstancesOfBookWithNotExistingBook() throws BookNotFoundException, BookInstanceNotFoundException {
        bookInstanceDao.getAllInstancesOfBook(testBook);
    }


    @Test(expected = BookInstanceNotFoundException.class)
    public void testGetAllInstancesOfBookWithNoInstances() throws BookNotFoundException, BookInstanceNotFoundException {
        bookInstanceDao.deleteBookInstance(dbInstance1);
        bookInstanceDao.deleteBookInstance(dbInstance2);
        bookInstanceDao.deleteBookInstance(dbInstance3);
        bookInstanceDao.getAllInstancesOfBook(book1);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testAllInstancesOfBookForNotFound() throws BookNotFoundException, BookInstanceNotFoundException {
        bookInstanceDao.getAllInstancesOfBook(testBook);
    }


    @Test
    public void testAvailableInstancesOfBook() throws BookNotFoundException, BookInstanceNotFoundException {
        Mockito.doReturn(this.book1).when(bookDaoXML).getBookByISBN(book1.getIsbn());
        Collection<BookInstance> actual = bookInstanceDao.getAvailableInstancesOfBook(book1);
        Collection<BookInstance> expected = new ArrayList<>();
        expected.add(this.dbInstance1);

        assertThat(actual.size(), is(1));
        assertEquals(expected, actual);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testAvailableInstancesOfBookForNotFound() throws BookNotFoundException, BookInstanceNotFoundException {
        bookInstanceDao.getAvailableInstancesOfBook(testBook);
    }


    @Test
    public void testupdateBookInstance() throws PersistenceException, BookInstanceNotFoundException, BookNotFoundException {
        Mockito.doReturn(this.book1).when(bookDaoXML).getBookByISBN(book1.getIsbn());
        Mockito.doReturn(this.book2).when(bookDaoXML).getBookByISBN(book2.getIsbn());
        this.dbInstance1.setBook(testBookinstance.getBook());
        this.dbInstance1.setLoaned(testBookinstance.isLoaned());

        bookInstanceDao.updateBookInstance(dbInstance1);

        BookInstance actual = bookInstanceDao.getBookInstanceByInventoryNumber(dbInstance1.getInventoryNumber());
        BookInstance expected = this.testBookinstance;

        assertEquals(actual.getBook(), expected.getBook());
        assertEquals(actual.isLoaned(), expected.isLoaned());
    }

    @Test
    public void testDeleteBookinstance() throws BookInstanceNotFoundException, BookNotFoundException {
        Mockito.doReturn(this.book1).when(bookDaoXML).getBookByISBN(book1.getIsbn());
        bookInstanceDao.deleteBookInstance(dbInstance1);
        List<BookInstance> bookInstances = bookInstanceDao.getAllBookInstances();

        assertThat(bookInstances, not(contains(dbInstance1)));
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
