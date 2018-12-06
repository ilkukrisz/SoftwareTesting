package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BorrowingDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BorrowingDAOTest {
    private BorrowingDaoXMLImpl dao;

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
        this.dao = new BorrowingDaoXMLImpl(this.databaseLocation, this.databaseLocation);
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
