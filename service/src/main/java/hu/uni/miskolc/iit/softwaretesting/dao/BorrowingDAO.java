package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.BorrowStatus;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;
import hu.uni.miskolc.iit.softwaretesting.model.Reader;

import java.util.Collection;

public interface BorrowingDAO {

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


}
