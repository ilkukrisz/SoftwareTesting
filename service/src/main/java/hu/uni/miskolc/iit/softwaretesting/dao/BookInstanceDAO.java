package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.AlreadyExistingBookInstanceException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.BookInstanceNotFoundException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.BookNotFoundException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.PersistenceException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;

import java.util.Collection;

public interface BookInstanceDAO {

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

}
