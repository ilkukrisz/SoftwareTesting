package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

public class LibrarianBookServiceImplTest  {
    @Mock
    private BookDAO daoMock;

    @InjectMocks
    private LibrarianBookServiceImpl service;

    private Book nullISBNBook = new Book("Bill Gates", "50 shades of fatal errors", null, 2000, Genre.Guide);

    public LibrarianBookServiceImplTest() throws InvalidPublishDateException {
        super();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookWithNullValue () throws AlreadyExistingBookException {
        doThrow(IllegalArgumentException.class).when(daoMock).createBook(null);
        service.addBook(null);
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateBookWithNullISBN () throws BookNotFoundException {
        doThrow(BookNotFoundException.class).when(daoMock).updateBook(this.nullISBNBook);
        service.updateBook(this.nullISBNBook);
    }

    @Test(expected = BookNotFoundException.class)
    public void testDeleteBookWithNullISBN () throws BookNotFoundException {
        doThrow(BookNotFoundException.class).when(daoMock).deleteBook(this.nullISBNBook);
        service.deleteBook(this.nullISBNBook);
    }

    @Test
    public void testCountZeroBook () throws BookNotFoundException {
        doThrow(BookNotFoundException.class).when(daoMock).getAllBooks();
        assertEquals(0, service.countBooks());
    }

    @Test
    public void testCountManyBooks () throws BookNotFoundException, InvalidPublishDateException {
        ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book ("Donald Trump", "Guns save lives", (long)111111, 2018, Genre.Crimi));
        books.add(new Book ( "Elon Musk", "The first human on Mars", (long)111222, 2017, Genre.Scifi));

        doReturn(books).when(daoMock).getAllBooks();

        assertEquals(2, service.countBooks());
    }

    @Test(expected = AlreadyExistingBookInstance.class)
    public void testAddAlreadyExistingBookInstance () throws InvalidPublishDateException, AlreadyExistingBookInstance {
        Book book = new Book ( "Elon Musk", "The first human on Mars", (long)111222, 2017, Genre.Scifi);
        BookInstance bookInstance = new BookInstance(262626, book, false);

        doThrow(AlreadyExistingBookInstance.class).when(daoMock).createBookInstance(bookInstance);

        service.addBookInstances(bookInstance);
    }

    @Test(expected = BookInstanceNotFound.class)
    public void testDeleteBookInstanceWithNullInventoryNumber () throws BookInstanceNotFound {
        doThrow(BookInstanceNotFound.class).when(daoMock).deleteBookInstance(null);
        service.deleteBookInstances(null);
    }

    @Test
    public void testLendBook () throws InvalidPublishDateException, NotExistingBorrowingException {
        Borrowing borrowing = getExampleBorrowing(BorrowStatus.REQUESTED);
        service.lendBook(borrowing);
        assertEquals(BorrowStatus.BORROWED, borrowing.getStatus());
    }

    @Test(expected = NotExistingBorrowingException.class)
    public void testLendBookWithNotExistingRequest () throws InvalidPublishDateException, NotExistingBorrowingException {
        Borrowing borrowing = getExampleBorrowing(BorrowStatus.REQUESTED);
        doThrow(NotExistingBorrowingException.class).when(daoMock).updateBorrowing(borrowing);
        service.lendBook(borrowing);
    }

    private Borrowing getExampleBorrowing (BorrowStatus status) throws InvalidPublishDateException {
        Reader reader = new Reader("tothgeza", new Password("alma"), "Geza", "Toth",
                "geza.toth@example.com", "06301234567");
        Book book = new Book ( "Elon Musk", "The first human on Mars", (long)111222, 2017, Genre.Scifi);
        BookInstance bookInstance = new BookInstance(919191, book, false);
        return new Borrowing(26262626, reader, new Date(1262304000), new Date(1272304000), status, bookInstance);
    }

    @Test(expected = NoBorrowingsFoundException.class)
    public void testListBorrowingsWhenNoBorrowingExist () throws NoBorrowingsFoundException {
        doThrow(NoBorrowingsFoundException.class).when(daoMock).getAllBorrowings();
        service.listBorrowings();
    }

    @Test
    public void testListBorrowingsWhenBorrowingsExist () throws InvalidPublishDateException, NoBorrowingsFoundException {
        ArrayList<Borrowing> borrowings = new ArrayList<Borrowing>();
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));
        borrowings.add(getExampleBorrowing(BorrowStatus.BORROWED));
        borrowings.add(getExampleBorrowing(BorrowStatus.RETURNED));

        doReturn(borrowings).when(daoMock).getAllBorrowings();

        assertEquals(borrowings, service.listBorrowings());
    }

    @Test
    public void testListRequestsWhenRequestsExist () throws InvalidPublishDateException, NotExistingBorrowingException {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));
        borrowings.add(getExampleBorrowing(BorrowStatus.REQUESTED));

        doReturn(borrowings).when(daoMock).getBorrowingsByStatus(BorrowStatus.REQUESTED);

        for (Borrowing borrowing : service.listRequests()) {
            if (borrowing.getStatus() != BorrowStatus.REQUESTED) {
                fail("listRequests() returns other borrowings than requests.");
            }
        }
    }
}
