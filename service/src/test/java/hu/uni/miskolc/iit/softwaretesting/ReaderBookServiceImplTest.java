package hu.uni.miskolc.iit.softwaretesting;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.impl.ReaderBookServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;

import java.util.*;


public class ReaderBookServiceImplTest {

    @Mock
    private BookDAO daoMock;

    @InjectMocks
    private ReaderBookServiceImpl service;

    private Book testBook;
    private Reader testReader;
    private BookInstance testBookInstanceAvailable;
    private Collection<Borrowing> testBorrowingCollection;
    private Borrowing testBorrowing;
    private Collection<Book> testBookCollection;
    private Map<String, String> testUserCredentials;


    @Before
    public void setUp() throws InvalidPublishDateException {
        this.testBookCollection = new ArrayList<>();
        this.testBook = new Book("George R. R. Martin", "Game of Thrones", 102410 ,2008, Genre.Fiction);
        this.testReader = new Reader("ilkukrisz", new Password(" "),"Ilku", "Krisztian",
                "noemail@gmail.com", "06205316259");

        testBookCollection.add(this.testBook);

        this.testBookInstanceAvailable = new BookInstance(10000, this.testBook, false);

        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.FEBRUARY, 10);

        Date creationDate = cal.getTime();
        cal.set(2018, Calendar.FEBRUARY, 20);
        Date expirationDate = cal.getTime();

        testBorrowing = new Borrowing(123456, testReader, creationDate, expirationDate, BorrowStatus.REQUESTED,
                this.testBookInstanceAvailable);

        testBorrowingCollection = new ArrayList<>();

        testBorrowingCollection.add(this.testBorrowing);

        this.testUserCredentials = new HashMap<>();
        this.testUserCredentials.put("ilkukrisz", " ");

        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = EmptyFieldException.class)
    public void testGetBooksByAuthorForEmptyField() throws BookNotFoundException, EmptyFieldException {
        service.getBooksByAuthor("");
    }


    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByAuthorWithNullValue() throws BookNotFoundException, EmptyFieldException {
        service.getBooksByAuthor(null);
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByAuthorForBookNotFound() throws BookNotFoundException, EmptyFieldException {
        when(daoMock.getBooksByAuthor("ThereisnoBook")).thenThrow(BookNotFoundException.class);
        service.getBooksByAuthor("ThereisnoBook");
    }

    @Test
    public void testGetBooksByAuthorForGoodValues() throws BookNotFoundException, EmptyFieldException {

        doReturn(this.testBookCollection).when(daoMock).getBooksByAuthor("George R. R. Martin");
        assertThat(service.getBooksByAuthor("George R. R. Martin"), contains(testBook));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBooksByCategoryWithEmptyFieldValue() throws EmptyFieldException, BookNotFoundException, NotExistingGenreException {
        service.getBooksByCategory(Genre.valueOf("wrongGenre"));
    }

    @Test
    public void testGetBooksByCategoryWithCorrectValue() throws EmptyFieldException, BookNotFoundException, NotExistingGenreException {
        doReturn(this.testBookCollection).when(daoMock).getBooksByGenre(Genre.Fiction);
        assertThat(service.getBooksByCategory(Genre.Fiction), contains(this.testBook));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByCategoryForBookNotFound() throws BookNotFoundException, EmptyFieldException, NotExistingGenreException {
        doThrow(BookNotFoundException.class).when(daoMock).getBooksByGenre(Genre.Other);
        service.getBooksByCategory(Genre.Other);
    }


    @Test
    public void testGetBooksByAvailabilityWithLegalValues() throws BookNotFoundException {

        doReturn(this.testBookCollection).when(daoMock).getAvailableBooks();
        assertThat(service.getBooksByAvailability(), contains(this.testBook));

    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByAvailabilityForBookNotFoundException() throws BookNotFoundException {
        doThrow(BookNotFoundException.class).when(daoMock).getAvailableBooks();
        service.getBooksByAvailability();
    }

    @Test(expected = EmptyFieldException.class)
    public void testGetAvailableBooksByTitleWithEmptyField() throws BookNotFoundException, EmptyFieldException {
        service.getAvailableBooksByTitle("");
    }

    @Test
    public void testGetAvailableBooksByTitleWithLegalValues() throws BookNotFoundException, EmptyFieldException {
        doReturn(this.testBookCollection).when(daoMock).getBooksByTitle("Game of Thrones");
        doReturn(this.testBookCollection).when(daoMock).getAvailableBooks();
        assertThat(service.getAvailableBooksByTitle("Game of Thrones"), contains(this.testBook));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetAvailableBooksByTitleForBookNotFoundExceptionBecauseTitle() throws BookNotFoundException, EmptyFieldException {
        doThrow(BookNotFoundException.class).when(daoMock).getBooksByTitle("Game of Thrones");
        doReturn(this.testBookCollection).when(daoMock).getAvailableBooks();
        service.getAvailableBooksByTitle("Game of Thrones");
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetAvailableBooksByTitleForBookNotFoundExceptionBecauseAvailability() throws BookNotFoundException, EmptyFieldException {
        doThrow(BookNotFoundException.class).when(daoMock).getAvailableBooks();
        service.getAvailableBooksByTitle("Game of Thrones");
    }

    @Test
    public void testGetBooksByYearWithLegalValues() throws BookNotFoundException, InvalidPublishDateException, EmptyFieldException {
        doReturn(this.testBookCollection).when(daoMock).getBooksByYear(2008);
        assertThat(service.getBooksByYear(2008), contains(this.testBook));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByYearBookNotFoundException() throws BookNotFoundException, InvalidPublishDateException, EmptyFieldException {
        doThrow(BookNotFoundException.class).when(daoMock).getBooksByYear(2008);
        service.getBooksByYear(2008);
    }

    @Test(expected = InvalidPublishDateException.class)
    public void testGetBooksByYearWithIllegalValues() throws BookNotFoundException, InvalidPublishDateException, EmptyFieldException {
        service.getBooksByYear(2020);
    }

    @Test(expected = InvalidPublishDateException.class)
    public void testGetBooksByYearWithBelowZeroValues() throws BookNotFoundException, InvalidPublishDateException, EmptyFieldException {
        service.getBooksByYear(-10);
    }

    @Test
    public void testRequestBookWithLegalValues() throws BookNotFoundException, BookInstanceNotFoundException, NoAvailableInstanceException, PersistenceException, NotExistingReaderException, NotExistingBorrowingException {
        ArrayList<BookInstance> availableInstance = new ArrayList<>();
        availableInstance.add(this.testBookInstanceAvailable);
        long newID = 20181203;

        doReturn(availableInstance).when(daoMock).getAvailableInstancesOfBook(this.testBook);
        doReturn(newID).when(daoMock).getNewID();

        service.requestBook(this.testBook, this.testReader);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testRequestBookWithNullValueForBook() throws BookNotFoundException, NoAvailableInstanceException, PersistenceException {
        service.requestBook(null, testReader);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testRequestBookWithNullValueForReader() throws BookNotFoundException, NoAvailableInstanceException, PersistenceException {
        service.requestBook(testBook, null);
    }

    @Test(expected = NoAvailableInstanceException.class)
    public void testRequestBookForNoAvailableInstancesException() throws NoAvailableInstanceException, BookNotFoundException, PersistenceException, BookInstanceNotFoundException {
        doReturn(new ArrayList<>()).when(daoMock).getAvailableInstancesOfBook(testBook);
        service.requestBook(testBook, testReader);
    }

    @Test(expected = BookNotFoundException.class)
    public void testRequestBookForBookNotFoundException() throws NoAvailableInstanceException, BookInstanceNotFoundException, BookNotFoundException, PersistenceException {
        doThrow(BookNotFoundException.class).when(daoMock).getAvailableInstancesOfBook(this.testBook);

        service.requestBook(testBook, testReader);
    }


    @Test
    public void testShowBorrowingsWithLegalValues() throws NotExistingReaderException, NotExistingBorrowingException {
        doReturn(this.testBorrowingCollection).when(daoMock).getBorrowingsOfReader(this.testReader);
        assertThat(service.showBorrowings(this.testReader), contains(this.testBorrowing));
    }

    @Test(expected = NotExistingReaderException.class)
    public void testShowBorrowingsWithNullReader() throws NotExistingReaderException, NotExistingBorrowingException {
        service.showBorrowings(null);
    }


    @Test(expected = NotExistingBorrowingException.class)
    public void testShowBorrowingsWithEmptyBorrowing() throws NotExistingReaderException, NotExistingBorrowingException {
        doReturn(new ArrayList<Borrowing>()).when(daoMock).getBorrowingsOfReader(this.testReader);
        service.showBorrowings(testReader);
    }


    @Test
    public void testGetAllBooksWithLegalValues() throws BookNotFoundException {
        doReturn(this.testBookCollection).when(daoMock).getAllBooks();
        assertThat(service.getAllBooks(), not(empty()));
    }


    @Test(expected = BookNotFoundException.class)
    public void testGetAllBooksForBookNotFoundException() throws BookNotFoundException {
        doThrow(BookNotFoundException.class).when(daoMock).getAllBooks();
        service.getAllBooks();
    }

    @Test
    public void testGetReaderCredentials() {
        doReturn(this.testUserCredentials).when(daoMock).getReaderCredentials();
        assertEquals(service.getReaderCredentials().get("ilkukrisz"), " ");
    }

}
