package hu.uni.miskolc.iit.softwaretesting.service;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.Genre;

import java.util.Collection;

public interface BookService {

    /**
     *
     * @return With the books stored in the database.
     */
    public Collection<Book> getAllBooks();

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
     * @param id the id of the book, which should be deleted.
     * @throws BookNotFoundException
     */
    void deleteBook(int id) throws BookNotFoundException;

    /**
     *
     * @return the number of books stored in the database.
     */
    int countBooks();

    /**
     *
     * @param author - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws BookNotFoundException
     * @throws EmptyFieldException
     */
    Collection<Book> getBooksByAuthor(String author) throws BookNotFoundException, EmptyFieldException;

    /**
     *
     * @param genre - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws NotExistingGenreException
     * @throws BookNotFoundException
     * @throws EmptyFieldException
     */
    Collection<Book> getBooksByCategory(Genre genre) throws NotExistingGenreException, BookNotFoundException, EmptyFieldException;

    /**
     *
     * @return The books filtered by availability.
     * @throws BookNotFoundException
     */
    Collection<Book> getBooksByAvailability() throws BookNotFoundException;

    /**
     *
     * @param title - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws EmptyFieldException
     * @throws BookNotFoundException
     */
    Collection<Book> getAvailableBooksByTitle(String title) throws EmptyFieldException, BookNotFoundException;

    /**
     *
     * @param year - Filtering argument.
     * @return The books filtered by the parameter.
     * @throws BookNotFoundException
     * @throws NotExistingGenreException
     * @throws EmptyFieldException
     */
    Collection<Book> getBooksByYear(int year) throws BookNotFoundException, NotExistingGenreException, EmptyFieldException;


}
