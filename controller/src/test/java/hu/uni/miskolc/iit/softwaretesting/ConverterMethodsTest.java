package hu.uni.miskolc.iit.softwaretesting;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import hu.uni.miskolc.iit.softwaretesting.converterMethods.ConverterMethods;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.*;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import static java.util.Calendar.getInstance;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ConverterMethodsTest {

    private Book testBook;
    private BookInstance testBookInstance;
    private Password testPassword;
    private Reader testReader;
    private Librarian testLibrarian;
    private Borrowing testBorrowing;
    private BookType testBookType = new BookType();
    private BookInstanceType testBookInstanceType = new BookInstanceType();
    private PasswordType testPasswordType = new PasswordType();
    private UserType testUserTypeReader = new UserType();
    private UserType testUserTypeLibrarian = new UserType();
    private BorrowingType testBorrowingType = new BorrowingType();
    private Collection<Book> testBookCollection = new ArrayList<>();
    private Collection<Borrowing> testBorrowingCollection = new ArrayList<>();
    private Collection<BorrowingType> testBorrowingTypeCollection = new ArrayList<>();


    @Before
    public void setUp() throws InvalidPublishDateException, DatatypeConfigurationException {
        testBook = new Book("Bela", "Test of Bela",123456789, 2015, Genre.Science);
        testBookInstance = new BookInstance(1234567, testBook,false);
        testPassword = new Password("asd");
        testReader = new Reader("Pisti", testPassword, "Nagy",  "Pista","asd@asd.hu","06705316528");
        testLibrarian = new Librarian("Joska", testPassword,  "Kiss","Joska", "dsa@dsa.hu", "06304216853");
        Calendar calendar = getInstance();
        calendar.add(Calendar.DATE,20);
        testBorrowing = new Borrowing(321654987, testReader, getInstance().getTime(), calendar.getTime(), BorrowStatus.REQUESTED, testBookInstance);



        testBookCollection.add(testBook);
        testBorrowingCollection.add(testBorrowing);
        testBookType.setAuthor("Bela");
        testBookType.setTitle("Test of Bela");
        testBookType.setIsbn(123456789);
        testBookType.setPublishDate(2015);
        testBookType.setGenre(Genre.Science.toString());
        testBookInstanceType.setBook(testBookType);
        testBookInstanceType.setInventoryNumber(1234567);
        testBookInstanceType.setIsLoaned(false);
        testPasswordType.setSaltStrength(2);
        testPasswordType.setHashedPassword("asd");
        testUserTypeReader.setEmail("asd@asd.hu");
        testUserTypeReader.setFirstName("Nagy");
        testUserTypeReader.setLastName("Pista");
        testUserTypeReader.setMobileNumber("06705316528");
        testUserTypeReader.setUsername("Pisti");
        testUserTypeReader.setPassword(testPasswordType);
        testUserTypeLibrarian.setEmail("dsa@dsa.hu");
        testUserTypeLibrarian.setFirstName("Kiss");
        testUserTypeLibrarian.setLastName("Joska");
        testUserTypeLibrarian.setMobileNumber("06304216853");
        testUserTypeLibrarian.setUsername("Joska");
        testUserTypeLibrarian.setPassword(testPasswordType);
        testBorrowingType.setBookInstance(testBookInstanceType);
        testBorrowingType.setBorrowID(321654987);
        testBorrowingType.setBorrowStatus(BorrowStatus.REQUESTED.toString());
        testBorrowingType.setReader(testUserTypeReader);
        XMLGregorianCalendar xmlgregcal = new XMLGregorianCalendarImpl();
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(getInstance().getTime());

        xmlgregcal.setYear(gregorianCalendar.get(Calendar.YEAR));
        xmlgregcal.setMonth(gregorianCalendar.get(Calendar.MONTH) + 1);
        xmlgregcal.setDay(gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        testBorrowingType.setCreationDate(xmlgregcal);

        gregorianCalendar.setTime(calendar.getTime());

        xmlgregcal.setYear(gregorianCalendar.get(Calendar.YEAR));
        xmlgregcal.setMonth(gregorianCalendar.get(Calendar.MONTH) + 1);
        xmlgregcal.setDay(gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        testBorrowingType.setExpirationDate(xmlgregcal);
        testBorrowingTypeCollection.add(testBorrowingType);

    }

    @Test
    public void testConstructor () {
        new ConverterMethods();
    }

    @Test
    public void testConvertBookToBookType() {
        assertTrue(testBookType.getAuthor().equals(((ArrayList<BookType>)ConverterMethods.convertBookToBookType(testBookCollection)).get(0).getAuthor()));
        assertTrue(testBookType.getGenre().equals(((ArrayList<BookType>)ConverterMethods.convertBookToBookType(testBookCollection)).get(0).getGenre()));
        assertTrue(testBookType.getTitle().equals(((ArrayList<BookType>)ConverterMethods.convertBookToBookType(testBookCollection)).get(0).getTitle()));
        assertTrue(testBookType.getIsbn() == (((ArrayList<BookType>)ConverterMethods.convertBookToBookType(testBookCollection)).get(0).getIsbn()));
        assertTrue(testBookType.getPublishDate() == (((ArrayList<BookType>)ConverterMethods.convertBookToBookType(testBookCollection)).get(0).getPublishDate()));
    }

    @Test
    public void testConvertBookInstanceToBookInstanceType() {
        assertTrue(testBookInstanceType.getInventoryNumber() == (ConverterMethods.convertBookInstanceToBookInstanceType(testBookInstance).getInventoryNumber()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getAuthor(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getAuthor()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getGenre(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getGenre()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getIsbn(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getIsbn()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getPublishDate(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getPublishDate()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getTitle(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getTitle()));
        assertTrue(testBookInstanceType.isIsLoaned() == (ConverterMethods.convertBookInstanceToBookInstanceType(testBookInstance).isIsLoaned()));
    }

    @Test
    public void testConvertBorrowingToBorrowingType() {
        testBorrowingType.setBookInstance(testBookInstanceType);
        testBorrowingType.setBorrowID(321654987);
        testBorrowingType.setBorrowStatus(BorrowStatus.REQUESTED.toString());
        testBorrowingType.setReader(testUserTypeReader);
        assertEquals(testBorrowingType.getBookInstance().getBook().getAuthor(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getAuthor()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getGenre(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getGenre()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getIsbn(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getIsbn()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getPublishDate(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getPublishDate()));
        assertEquals(testBorrowingType.getBookInstance().getBook().getTitle(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getBook().getTitle()));
        assertEquals(testBorrowingType.getBookInstance().getInventoryNumber(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().getInventoryNumber()));
        assertEquals(testBorrowingType.getBookInstance().isIsLoaned(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBookInstance().isIsLoaned()));
        assertEquals(testBorrowingType.getBorrowID(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBorrowID()));
        assertEquals(testBorrowingType.getBorrowStatus(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getBorrowStatus()));
        assertEquals(testBorrowingType.getReader().getEmail(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getReader().getEmail()));
        assertEquals(testBorrowingType.getReader().getFirstName(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getReader().getFirstName()));
        assertEquals(testBorrowingType.getReader().getLastName(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getReader().getLastName()));
        assertEquals(testBorrowingType.getReader().getMobileNumber(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getReader().getMobileNumber()));
        assertEquals(testBorrowingType.getReader().getUsername(),(((ArrayList<BorrowingType>)ConverterMethods.convertBorrowingToBorrowingType(testBorrowingCollection)).get(0).getReader().getUsername()));
    }

    @Test
    public void testConvertReaderToUserTypeReader() {
        assertTrue(testUserTypeReader.getEmail().equals(ConverterMethods.convertUserTypeToReader(testUserTypeReader).getEmail()));
        assertTrue(testUserTypeReader.getFirstName().equals(ConverterMethods.convertUserTypeToReader(testUserTypeReader).getFirstName()));
        assertTrue(testUserTypeReader.getLastName().equals(ConverterMethods.convertUserTypeToReader(testUserTypeReader).getLastName()));
        assertTrue(testUserTypeReader.getMobileNumber().equals(ConverterMethods.convertUserTypeToReader(testUserTypeReader).getMobileNumber()));
        assertTrue(testUserTypeReader.getUsername().equals(ConverterMethods.convertUserTypeToReader(testUserTypeReader).getUsername()));
    }

    @Test
    public void testConvertBookTypeToBook() throws InvalidPublishDateException {
        assertEquals(testBook,ConverterMethods.convertBookTypeToBook(testBookType));
    }

    @Test
    public void testConvertBookInstanceTypeToBookInstance() throws InvalidPublishDateException {
        assertEquals(testBookInstance,ConverterMethods.convertBookInstanceTypeToBookInstance(testBookInstanceType));
    }

    @Test
    public void testConvertUserTypeToLibrarian() {
        assertEquals(testLibrarian,ConverterMethods.convertUserTypeToLibrarian(testUserTypeLibrarian));
    }

    @Test
    public void testConvertUserTypeToReader() {
        assertEquals(testReader,ConverterMethods.convertUserTypeToReader(testUserTypeReader));
    }
}
