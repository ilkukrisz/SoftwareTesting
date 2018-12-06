package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;

import java.util.Collection;
import java.util.Map;

public interface BookDAO {
    /**
     * Creates a new book record in database.
     * @param book Data container of the new book.
     */
    public void createBook(Book book) throws AlreadyExistingBookException, PersistenceException;

    /**
     * Returns all books from database.
     */
    public Collection<Book> getAllBooks() throws BookNotFoundException;

    /**
     * Returns all borrowable books.
     */
    public Collection<Book> getAvailableBooks() throws BookNotFoundException;

    /**
     * Returns all books with the given title.
     * @param title Title of the book.
     */
    public Collection<Book> getBooksByTitle(String title) throws BookNotFoundException;

    /**
     * Returns the book with the given ISBN.
     * @param ISBN ISBN number of the searched book.
     */
    public Book getBookByISBN(long ISBN) throws BookNotFoundException;

    /**
     * Returns the book with the given author.
     * @param author The author of the book.
     */
    public Collection<Book> getBooksByAuthor(String author) throws BookNotFoundException;

    /**
     * Updates the book with the given information.
     * @param book Data container of the new book data. ISBN field have to contain
     *             the desired book's unique ISBN number.
     */

    /**
     * Returns the books with the given genre.
     * @param genre The genre of the book.
     */
    public Collection<Book> getBooksByGenre(Genre genre) throws BookNotFoundException;

    /**
     * Returns the books with the given publish date.
     * @param year The publish date of the books.
     */
    public Collection<Book> getBooksByYear(int year) throws BookNotFoundException;


    public void updateBook(Book book) throws BookNotFoundException, PersistenceException;

    /**
     * Deletes the book.
     * @param book The book object. ISBN field have to contain the erasable book's unique ISBN number.
     */
    public void deleteBook(Book book) throws BookNotFoundException, PersistenceException;

    /**
     * Returns an id which is not used yet.
     */
    public long getNewID();

    public Map<String, String> getReaderCredentials ();

    public Map<String, String> getLibrarianCredentials ();

}
