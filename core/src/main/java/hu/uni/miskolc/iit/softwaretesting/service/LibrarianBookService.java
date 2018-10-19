package hu.uni.miskolc.iit.softwaretesting.service;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;

import java.util.Collection;

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
    public void addBook(Book book) throws AlreadyExistingBookException, WrongISBNException, EmptyFieldException, InvalidPublishDateException, NotExistingGenreException;

    /**
     *
     * @param book - The book which should be updated.
     * @throws WrongISBNException
     * @throws EmptyFieldException
     * @throws InvalidPublishDateException
     * @throws NotExistingGenreException
     */
    void updateBook(Book book) throws WrongISBNException, EmptyFieldException, InvalidPublishDateException, NotExistingGenreException;

    /**
     *
     * @param isbn the ISBN of the book, which should be deleted.
     * @throws BookNotFoundException
     */
    void deleteBook(int isbn) throws BookNotFoundException;

    /**
     *
     * @return the number of books stored in the database.
     */
    int countBooks();


    /**
     *
     * @param bookInstance the new instance which should be added to the database
     * @throws AlreadyExistingBookInstance
     * @throws EmptyFieldException
     */
    public void addBookInstances(BookInstance bookInstance) throws AlreadyExistingBookInstance, EmptyFieldException;

    /**
     *
     * @param inventoryNumber the book instance that should be deleted from the databse.
     */
    public void deleteBookInstances(long inventoryNumber);

    /**
     *
     * @param borrow the borrow which will be examined and completed
     * @throws NotExistingBorrowingException
     */
    public void lendBook(Borrowing borrow) throws NotExistingBorrowingException;

    /**
     *
     * @return the list of borrowings
     */
    public Collection<Borrowing> listBorrowings();

    /**
     *
     * @return the list of requests
     */
    public Collection<Borrowing> listRequests();




}
