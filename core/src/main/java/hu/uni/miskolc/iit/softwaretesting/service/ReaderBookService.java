package hu.uni.miskolc.iit.softwaretesting.service;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;
import hu.uni.miskolc.iit.softwaretesting.model.Genre;
import hu.uni.miskolc.iit.softwaretesting.model.Reader;

import java.util.Collection;

public interface ReaderBookService extends BookService {

    /**
     *
     * @param author - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws BookNotFoundException
     * @throws EmptyFieldException
     */
    public Collection<Book> getBooksByAuthor(String author) throws BookNotFoundException, EmptyFieldException;

    /**
     *
     * @param genre - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws NotExistingGenreException
     * @throws BookNotFoundException
     * @throws EmptyFieldException
     */
    public Collection<Book> getBooksByCategory(Genre genre) throws NotExistingGenreException, BookNotFoundException, EmptyFieldException;

    /**
     *
     * @return The books filtered by availability.
     * @throws BookNotFoundException
     */
    public Collection<Book> getBooksByAvailability() throws BookNotFoundException;

    /**
     *
     * @param title - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws EmptyFieldException
     * @throws BookNotFoundException
     */
    public Collection<Book> getAvailableBooksByTitle(String title) throws EmptyFieldException, BookNotFoundException;

    /**
     *
     * @param year - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws BookNotFoundException
     * @throws NotExistingGenreException
     * @throws EmptyFieldException
     */
    public Collection<Book> getBooksByYear(int year) throws BookNotFoundException, NotExistingGenreException, EmptyFieldException;

    /**
     *
     * @param book the book which is required by the reader
     * @throws NoAvailableInstanceException
     */
    public void requestBook(Book book, Reader reader) throws NoAvailableInstanceException, BookNotFoundException;


    /**
     *
     * @return the borrowings that are currently at the user
     */
    public Collection<Borrowing> showBorrowings(Reader reader) throws NotExistingReaderException, NotExistingBorrowingException;
}
