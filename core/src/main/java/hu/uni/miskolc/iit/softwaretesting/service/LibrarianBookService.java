package hu.uni.miskolc.iit.softwaretesting.service;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;

import java.util.Collection;
import java.util.Map;

public interface LibrarianBookService extends BookService {

    /**
     *
     * @param book - The book which should be added to the database
     * @throws AlreadyExistingBookException
     * @throws WrongISBNException
     * @throws EmptyFieldException
     * @throws InvalidPublishDateException
     * @throws NotExistingGenreException
     */
    public void addBook(Book book) throws AlreadyExistingBookException, WrongISBNException, EmptyFieldException, InvalidPublishDateException, NotExistingGenreException, PersistenceException;

    /**
     *
     * @param book - The book which should be updated.
     * @throws WrongISBNException
     * @throws EmptyFieldException
     * @throws InvalidPublishDateException
     * @throws NotExistingGenreException
     * @throws BookNotFoundException
     */
    void updateBook(Book book) throws BookNotFoundException, WrongISBNException, EmptyFieldException, InvalidPublishDateException, NotExistingGenreException, PersistenceException;

    /**
     *
     * @param book the book, which should be deleted.
     * @throws BookNotFoundException
     */
    void deleteBook(Book book) throws BookNotFoundException, PersistenceException;

    /**
     *
     * @return the number of books stored in the database.
     */
    int countBooks();


    /**
     *
     * @param bookInstance the new instance which should be added to the database
     * @throws AlreadyExistingBookInstanceException
     * @throws EmptyFieldException
     */
    public void addBookInstances(BookInstance bookInstance) throws AlreadyExistingBookInstanceException, EmptyFieldException, PersistenceException;

    /**
     *
     * @param bookInstance the book instance that should be deleted from the database.
     * @throws BookInstanceNotFoundException
     */
    public void deleteBookInstances(BookInstance bookInstance) throws BookInstanceNotFoundException;

    /**
     *
     * @param borrow the borrow which will be examined and completed
     * @throws NotExistingBorrowingException
     */
    public void lendBook(Borrowing borrow) throws NotExistingBorrowingException, PersistenceException;

    /**
     *
     * @return the list of borrowings
     * @throws NoBorrowingsFoundException
     */
    public Collection<Borrowing> listBorrowings() throws NoBorrowingsFoundException;

    /**
     *
     * @return the list of requests
     * @throws NotExistingBorrowingException
     */
    public Collection<Borrowing> listRequests() throws NotExistingBorrowingException;

    public Map<String, String> getLibrarianCredentials();
}
