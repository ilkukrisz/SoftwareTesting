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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


public class ReaderBookServiceImplTest {

    @Mock
    private BookDAO daoMock;

    @InjectMocks
    private ReaderBookServiceImpl service;

    private Book book = new Book("Alma", "Barack", (long) 102410,
            2008, Genre.valueOf("Crimi"));
    private Reader reader = new Reader("ilkukrisz", new Password("alma"),
            "Ilku", "Krisztian", "alma@alma.hu", "06705382835");


    public ReaderBookServiceImplTest() throws InvalidPublishDateException {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ReaderBookServiceImpl(daoMock);
    }

    @Test(expected = EmptyFieldException.class)
    public void testGetBooksByAuthorForEmptyField() throws BookNotFoundException, EmptyFieldException {
        service.getBooksByAuthor("");
    }

    @Test(expected = NullPointerException.class)
    public void testGetBooksByAuthorWithNullValue() throws BookNotFoundException, EmptyFieldException {
        service.getBooksByAuthor(null);
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByAuthorForBookNotFound() throws BookNotFoundException, EmptyFieldException {
        when(daoMock.getBooksByAuthor("awehbrha")).thenThrow(BookNotFoundException.class);
        service.getBooksByAuthor("awehbrha");
    }

    @Test
    public void testGetBooksByAuthorForGoodValues() throws BookNotFoundException, EmptyFieldException, InvalidPublishDateException {
        when(daoMock.getBooksByAuthor("microsoft")).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection books = new ArrayList();
                Object[] arguments = invocationOnMock.getArguments();

                if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                    books.add(new Book(arguments[0].toString(), "asd", (long) 102410,
                            2008, Genre.Science));

                    return books;
                }
                return null;
            }
        });

        Collection<Book> expected = new ArrayList<>();
        expected.add(new Book("microsoft", "asd", (long)102410,
                2008, Genre.Science));

        assertTrue(expected.equals(service.getBooksByAuthor("microsoft")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBooksByCategoryWithEmptyFieldValue() throws EmptyFieldException, BookNotFoundException, NotExistingGenreException {
        service.getBooksByCategory(Genre.valueOf("asd"));
    }

    @Test
    public void testGetBooksByCategoryWithCorrectValue() throws EmptyFieldException, BookNotFoundException, NotExistingGenreException, InvalidPublishDateException {
        when(daoMock.getBooksByGenre(Genre.Crimi)).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection books = new ArrayList();
                Object[] arguments = invocationOnMock.getArguments();

                if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                    books.add(new Book("asd", "asd", (long) 102410,
                            2008, Genre.valueOf("Crimi")));

                    books.add(new Book("wert", "awhrb", (long) 785432,
                            2003, Genre.valueOf("Crimi")));

                    return books;
                }
                return null;
            }
        });

        Collection<Book> excepted = new ArrayList<>();
        excepted.add(new Book("asd", "asd", (long) 102410,
                2008, Genre.Crimi));
        excepted.add(new Book("wert", "awhrb", (long) 785432,
                2003, Genre.Crimi));

        assertEquals(excepted, service.getBooksByCategory(Genre.Crimi));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByCategoryForBookNotFound() throws BookNotFoundException, EmptyFieldException {
        when(service.getBooksByCategory(Genre.Natural)).thenThrow(BookNotFoundException.class);
        service.getBooksByCategory(Genre.Natural);
    }


    @Test
    public void testGetBooksByAvailabilityWithLegalValues() throws BookNotFoundException, InvalidPublishDateException {
        when(daoMock.getAvailableBooks()).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection books = new ArrayList();

                books.add(new Book("Alma", "Korte", (long) 102410,
                        2008, Genre.valueOf("Crimi")));

                books.add(new Book("Alma", "Szilva", (long) 785432,
                        2003, Genre.valueOf("Fiction")));

                return books;
            }
        });

        Collection<Book> excepted = new ArrayList<>();
        excepted.add(new Book("Alma", "Korte", (long) 102410,
                2008, Genre.valueOf("Crimi")));

        excepted.add(new Book("Alma", "Szilva", (long) 785432,
                2003, Genre.valueOf("Fiction")));

        assertEquals(excepted, service.getBooksByAvailability());

    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByAvailabilityForBookNotFoundException() throws BookNotFoundException {
        when(daoMock.getAvailableBooks()).thenThrow(BookNotFoundException.class);
        service.getBooksByAvailability();
    }

    @Test(expected = EmptyFieldException.class)
    public void testGetAvailableBooksByTitleWithEmptyField() throws BookNotFoundException, EmptyFieldException {
        service.getAvailableBooksByTitle("");
    }

    @Test
    public void testGetAvailableBooksByTitleWithLegalValues() throws BookNotFoundException, EmptyFieldException, InvalidPublishDateException {
        when(daoMock.getAvailableBooks()).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection books = new ArrayList();

                books.add(new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")));

                books.add(new Book("Alma", "Szilva", (long) 785432,
                        2003, Genre.valueOf("Fiction")));

                return books;
            }
        });

        when(daoMock.getBooksByTitle("Barack")).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection books = new ArrayList();

                books.add(new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")));
                return books;
            }
        });

        Collection<Book> excepted = new ArrayList<>();
        excepted.add(new Book("Alma", "Barack", (long) 102410,
                2008, Genre.valueOf("Crimi")));

        assertEquals(excepted, service.getAvailableBooksByTitle("Barack"));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetAvailableBooksByTitleForBookNotFoundExceptionBecauseTitle() throws BookNotFoundException, EmptyFieldException {
        when(daoMock.getBooksByTitle("Barack")).thenThrow(BookNotFoundException.class);
        service.getAvailableBooksByTitle("Barack");
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetAvailableBooksByTitleForBookNotFoundExceptionBecauseAvailability() throws BookNotFoundException, EmptyFieldException {
        when(daoMock.getAvailableBooks()).thenThrow(BookNotFoundException.class);
        service.getAvailableBooksByTitle("Barack");
    }

    @Test
    public void testGetBooksByYearWithLegalValues() throws BookNotFoundException, InvalidPublishDateException, EmptyFieldException {
        when(daoMock.getBooksByYear(2000)).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection books = new ArrayList();

                books.add(new Book("Alma", "Barack", (long) 102410,
                        2000, Genre.valueOf("Crimi")));
                return books;
            }
        });

        Collection<Book> excepted = new ArrayList<>();
        excepted.add(new Book("Alma", "Barack", (long) 102410,
                2000, Genre.valueOf("Crimi")));

        assertEquals(excepted, service.getBooksByYear(2000));
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBooksByYearBookNotFoundException() throws BookNotFoundException, InvalidPublishDateException, EmptyFieldException {
        when(daoMock.getBooksByYear(2017)).thenThrow(new BookNotFoundException());
        service.getBooksByYear(2017);
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
    public void testRequestBookWithLegalValues() throws InvalidPublishDateException, BookNotFoundException, BookInstanceNotFound, NoAvailableInstanceException, PersistenceException {
        Book book = new Book("Alma", "Barack", (long) 102410,
                2008, Genre.valueOf("Crimi"));
        Reader reader = new Reader("ilkukrisz", new Password("alma"),
                "Ilku", "Krisztian", "alma@alma.hu", "06705382835");

        when(daoMock.getAvailableInstancesOfBook(book)).thenAnswer(new Answer<Collection<BookInstance>>() {
            @Override
            public Collection<BookInstance> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection<BookInstance> bookInstances = new ArrayList<>();
                bookInstances.add(new BookInstance(6778654, new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")), false));
                bookInstances.add(new BookInstance(111111, new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")), false));

                bookInstances.add(new BookInstance(222222, new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")), true));

                return bookInstances;
            }

        });

        service.requestBook(book, reader);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testRequestBookWithNullValueForBook() throws InvalidPublishDateException, BookNotFoundException, BookInstanceNotFound, NoAvailableInstanceException, PersistenceException {

        when(daoMock.getAvailableInstancesOfBook(book)).thenAnswer(new Answer<Collection<BookInstance>>() {
            @Override
            public Collection<BookInstance> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection<BookInstance> bookInstances = new ArrayList<>();
                bookInstances.add(new BookInstance(6778654, new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")), false));
                return bookInstances;
            }

        });

        service.requestBook(null, reader);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testRequestBookWithNullValueForReader() throws InvalidPublishDateException, BookNotFoundException, BookInstanceNotFound, NoAvailableInstanceException, PersistenceException {

        when(daoMock.getAvailableInstancesOfBook(book)).thenAnswer(new Answer<Collection<BookInstance>>() {
            @Override
            public Collection<BookInstance> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection<BookInstance> bookInstances = new ArrayList<>();
                bookInstances.add(new BookInstance(6778654, new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")), false));
                return bookInstances;
            }

        });

        service.requestBook(book, null);
    }

    @Test(expected = NoAvailableInstanceException.class)
    public void testRequestBookForNoAvailableInstancesException() throws NoAvailableInstanceException, BookNotFoundException, PersistenceException {
        service.requestBook(book, reader);
    }

    @Test(expected = BookNotFoundException.class)
    public void testRequestBookForBookNotFoundException() throws NoAvailableInstanceException, BookInstanceNotFound, BookNotFoundException, PersistenceException {
        when(daoMock.getAvailableInstancesOfBook(book)).thenThrow(BookNotFoundException.class);
        service.requestBook(book, reader);
    }


    @Test
    public void testShowBorrowingsWithLegalValues() throws NotExistingReaderException, NotExistingBorrowingException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 10);
        Date expirationDate = new Date(cal.getTime().getTime());
        when(daoMock.getBorrowingsOfReader(reader)).thenAnswer(new Answer<Collection<Borrowing>>() {
            @Override
            public Collection<Borrowing> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection<Borrowing> result = new ArrayList<>();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 10);
                Date expirationDate = new Date(cal.getTime().getTime());

                result.add(new Borrowing(7123, reader, new Date(Calendar.getInstance().getTime().getTime()),
                        expirationDate, BorrowStatus.BORROWED, new BookInstance(2346, book, true)));

                result.add(new Borrowing(6785, reader, new Date(Calendar.getInstance().getTime().getTime()),
                        expirationDate, BorrowStatus.BORROWED, new BookInstance(324, book, true)));

                return result;
            }
        });

        Collection<Borrowing> expected = new ArrayList<>();
        expected.add(new Borrowing(7123, reader, new Date(Calendar.getInstance().getTime().getTime()),
                expirationDate, BorrowStatus.BORROWED, new BookInstance(2346, book, true)));

        expected.add(new Borrowing(6785, reader, new Date(Calendar.getInstance().getTime().getTime()),
                expirationDate, BorrowStatus.BORROWED, new BookInstance(324, book, true)));

        assertTrue(expected.equals(service.showBorrowings(reader)));
    }

    @Test(expected = NotExistingBorrowingException.class)
    public void testShowBorrowingsWithNullForEmptyBorrowing() throws NotExistingReaderException, NotExistingBorrowingException {
        service.showBorrowings(reader);
    }

    @Test
    public void testGetAllBooksWithLegalValues() throws BookNotFoundException, InvalidPublishDateException {
        when(daoMock.getAllBooks()).thenAnswer(new Answer<Collection<Book>>() {
            @Override
            public Collection<Book> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Collection<Book> books = new ArrayList<>();
                books.add(new Book("Alma", "Barack", (long) 102410,
                        2008, Genre.valueOf("Crimi")));

                books.add(new Book("Alma", "Szilva", (long) 785432,
                        2003, Genre.valueOf("Fiction")));

                return books;
            }

        });

        Collection<Book> expected = new ArrayList<>();
        expected.add(new Book("Alma", "Barack", (long) 102410,
                2008, Genre.valueOf("Crimi")));

        expected.add(new Book("Alma", "Szilva", (long) 785432,
                2003, Genre.valueOf("Fiction")));

        assertEquals(expected, service.getAllBooks());
    }


    @Test(expected = BookNotFoundException.class)
    public void testGetAllBooksForBookNotFoundException() throws BookNotFoundException {
        when(daoMock.getAllBooks()).thenThrow(BookNotFoundException.class);
        service.getAllBooks();
    }



}
