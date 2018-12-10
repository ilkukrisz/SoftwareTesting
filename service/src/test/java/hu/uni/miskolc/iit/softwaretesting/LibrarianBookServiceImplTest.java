package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BookInstanceDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BorrowingDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.impl.LibrarianBookServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class LibrarianBookServiceImplTest  {
    @Mock
    private BookDAO daoMock;

    @Mock
    private BookInstanceDAO IdaoMock;

    @Mock
    private BorrowingDAO BdaoMock;

    @InjectMocks
    private LibrarianBookServiceImpl service;

    @Mock
    private Book testBook;

    @Mock
    private BookInstance testBookInstance;

    @Mock
    private Reader testReader;

    public LibrarianBookServiceImplTest() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        this.testBook = new Book("Bill Gates", "50 shades of fatal errors", 35472, 2000, Genre.Guide);
        this.testBookInstance = new BookInstance(123456, this.testBook, false);
        this.testReader = new Reader("tothgeza", new Password("alma"), "Geza", "Toth",
                "geza.toth@example.com", "06301234567");

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddBook () throws AlreadyExistingBookException, PersistenceException {
        doNothing().when(daoMock).createBook(testBook);
        service.addBook(testBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookWithNullValue () throws AlreadyExistingBookException, PersistenceException {
        doThrow(IllegalArgumentException.class).when(daoMock).createBook(null);
        service.addBook(null);
    }

    @Test
    public void testUpdateBook () throws BookNotFoundException, PersistenceException {
        this.testBook.setAuthor("A B C");
        doNothing().when(daoMock).updateBook(testBook);
        service.updateBook(testBook);
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateBookWithNullISBN () throws BookNotFoundException, PersistenceException {
        doThrow(BookNotFoundException.class).when(daoMock).updateBook(this.testBook);
        service.updateBook(this.testBook);
    }

    @Test
    public void testDeleteBook () throws BookNotFoundException, PersistenceException {
        doNothing().when(daoMock).deleteBook(testBook);
        service.deleteBook(testBook);
    }

    @Test(expected = BookNotFoundException.class)
    public void testDeleteBookWithNullISBN () throws BookNotFoundException, PersistenceException {
        doThrow(BookNotFoundException.class).when(daoMock).deleteBook(this.testBook);
        service.deleteBook(this.testBook);
    }

    @Test
    public void testCountZeroBook () throws BookNotFoundException {
        doThrow(BookNotFoundException.class).when(daoMock).getAllBooks();
        assertEquals(0, service.countBooks());
    }

    @Test
    public void testCountManyBooks () throws BookNotFoundException, InvalidPublishDateException {
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book ("Donald Trump", "Guns save lives", (long)111111, 2018, Genre.Crimi));
        books.add(new Book ( "Elon Musk", "The first human on Mars", (long)111222, 2017, Genre.Scifi));

        doReturn(books).when(daoMock).getAllBooks();

        assertEquals(2, service.countBooks());
    }

    @Test
    public void testAddBookInstance () throws PersistenceException, AlreadyExistingBookInstanceException {
        doNothing().when(IdaoMock).createBookInstance(testBookInstance);
        service.addBookInstances(testBookInstance);
    }

    @Test(expected = AlreadyExistingBookInstanceException.class)
    public void testAddAlreadyExistingBookInstance () throws InvalidPublishDateException, AlreadyExistingBookInstanceException, PersistenceException {
        Book book = new Book ( "Elon Musk", "The first human on Mars", (long)111222, 2017, Genre.Scifi);
        BookInstance bookInstance = new BookInstance(262626, book, false);

        doThrow(AlreadyExistingBookInstanceException.class).when(IdaoMock).createBookInstance(bookInstance);

        service.addBookInstances(bookInstance);
    }

    @Test
    public void testDeleteBookInstance () throws BookInstanceNotFoundException {
        doNothing().when(IdaoMock).deleteBookInstance(testBookInstance);
        service.deleteBookInstances(testBookInstance);
    }

    @Test(expected = BookInstanceNotFoundException.class)
    public void testDeleteBookInstanceWithNullInventoryNumber () throws BookInstanceNotFoundException {
        doThrow(BookInstanceNotFoundException.class).when(IdaoMock).deleteBookInstance(null);
        service.deleteBookInstances(null);
    }

    @Test
    public void testLendBook () throws NotExistingBorrowingException, PersistenceException {
        Borrowing borrowing = getExampleBorrowing(BorrowStatus.REQUESTED);
        service.lendBook(borrowing);
        assertEquals(BorrowStatus.BORROWED, borrowing.getStatus());
    }

    @Test(expected = NotExistingBorrowingException.class)
    public void testLendBookWithNotExistingRequest () throws NotExistingBorrowingException, PersistenceException {
        Borrowing borrowing = getExampleBorrowing(BorrowStatus.REQUESTED);
        doThrow(NotExistingBorrowingException.class).when(BdaoMock).updateBorrowing(borrowing);
        service.lendBook(borrowing);
    }

    private Borrowing getExampleBorrowing (BorrowStatus status) {
        return new Borrowing(26262626, testReader, new Date(1262304000), new Date(1272304000), status, testBookInstance);
    }

    @Test(expected = NoBorrowingsFoundException.class)
    public void testListBorrowingsWhenNoBorrowingExist () throws NoBorrowingsFoundException {
        doThrow(NoBorrowingsFoundException.class).when(BdaoMock).getAllBorrowings();
        service.listBorrowings();
    }

    @Test
    public void testListBorrowingsWhenBorrowingsExist () throws NoBorrowingsFoundException {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));
        borrowings.add(getExampleBorrowing(BorrowStatus.BORROWED));
        borrowings.add(getExampleBorrowing(BorrowStatus.RETURNED));

        doReturn(borrowings).when(BdaoMock).getAllBorrowings();

        assertEquals(borrowings, service.listBorrowings());
    }

    @Test
    public void testListRequestsWhenRequestsExist () throws NotExistingBorrowingException {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));

        doReturn(borrowings).when(BdaoMock).getBorrowingsByStatus(BorrowStatus.REQUESTED);

        for (Borrowing borrowing : service.listRequests()) {
            if (borrowing.getStatus() != BorrowStatus.REQUESTED) {
                fail("listRequests() returns other borrowings than requests.");
            }
        }
    }
}
