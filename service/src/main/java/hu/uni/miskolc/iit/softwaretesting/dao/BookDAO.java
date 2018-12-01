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
     * Returns the book with the given ISBN.
     * @param author The author of the book.
     */
    public Collection<Book> getBooksByAuthor(String author) throws BookNotFoundException;

    /**
     * Updates the book with the given information.
     * @param book Data container of the new book data. ISBN field have to contain
     *             the desired book's unique ISBN number.
     */
    public void updateBook(Book book) throws BookNotFoundException, PersistenceException;

    /**
     * Deletes the book.
     * @param book The book object. ISBN field have to contain the erasable book's unique ISBN number.
     */
    public void deleteBook(Book book) throws BookNotFoundException, PersistenceException;


    /**
     * Creates a new book instance in database.
     * @param bookInstance Data container of the new book instance.
     */
    public void createBookInstance(BookInstance bookInstance) throws AlreadyExistingBookInstanceException, PersistenceException;

    /**
     * Returns all book instances from the database.
     */
    public Collection<BookInstance> getAllBookInstances() throws BookInstanceNotFoundException;

    /**
     * Returns all instances of the given book.
     * @param book The book object, which we search instances of.
     */
    public Collection<BookInstance> getAllInstancesOfBook(Book book) throws BookNotFoundException,
            BookInstanceNotFoundException;

    /**
     * Returns all borrowable instances of the given book.
     * @param book The book object, which we search instances of.
     */
    public Collection<BookInstance> getAvailableInstancesOfBook(Book book) throws BookNotFoundException,
            BookInstanceNotFoundException;

    /**
     * Returns the book instance with the given inventoryNumber.
     * @param inventoryNumber Inventory number of the book instance.
     */
    public BookInstance getBookInstanceByInventoryNumber(long inventoryNumber) throws BookInstanceNotFoundException, BookNotFoundException;

    /**
     * Updates the book instance in the database.
     * @param bookInstance Data container of the new book instance.
     *                     InventoryNumber field must be set to the desired book instance's to update.
     */
    public void updateBookInstance(BookInstance bookInstance) throws BookInstanceNotFoundException, PersistenceException;

    /**
     * Deletes the book instance from the database.
     * @param bookInstance BookInstance object with the eraseable book instance's inventory number.
     */
    public void deleteBookInstance(BookInstance bookInstance) throws BookInstanceNotFoundException;


    /**
     * Creates a new borrowing in the database.
     * @param borrowing Data container of the new borrowing.
     */
    public void createBorrowing(Borrowing borrowing) throws AlreadyExistingBorrowingException, PersistenceException;

    /**
     * Returns all of the borrowings from the database.
     */
    public Collection<Borrowing> getAllBorrowings() throws NoBorrowingsFoundException;

    /**
     * Returns all borrowings with the given status.
     * @param status The searched borrowing status.
     */
    public Collection<Borrowing> getBorrowingsByStatus(BorrowStatus status) throws NotExistingBorrowingException;

    /**
     * Returns all borrowings of the given reader.
     * @param reader The searched reader.
     */
    public Collection<Borrowing> getBorrowingsOfReader(Reader reader) throws NotExistingBorrowingException,
                                                                                NotExistingReaderException;

    /**
     * Updates borrowing in the database.
     * @param borrowing The borrowing object to update.
     *                  BorrowID field have to contain the unique id of the borrowing to update.
     */
    public void updateBorrowing(Borrowing borrowing) throws NotExistingBorrowingException, PersistenceException;

    /**
     * Deletes the given borrowing from the database.
     * @param borrowing The borrowing object to delete.
     *                  BorrowID field have to contain the unique id of the borrowing to delete.
     */
    public void deleteBorrowing(Borrowing borrowing) throws NotExistingBorrowingException;

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

    /**
     * Returns an id which is not used yet.
     */
    public long getNewID();

    public Map<String, String> getReaderCredentials ();

    public Map<String, String> getLibrarianCredentials ();

}
