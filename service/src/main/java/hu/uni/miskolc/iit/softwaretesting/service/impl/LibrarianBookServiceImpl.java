package hu.uni.miskolc.iit.softwaretesting.service.impl;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;
import hu.uni.miskolc.iit.softwaretesting.model.BorrowStatus;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class LibrarianBookServiceImpl extends BookServiceImpl implements LibrarianBookService {


    public void addBook(Book book) throws AlreadyExistingBookException, PersistenceException {
        try {
            bookDAO.createBook(book);
        } catch (AlreadyExistingBookException e) {
            throw new AlreadyExistingBookException(e);
        } catch (PersistenceException e) {
            throw new PersistenceException(e);
        }
    }

    public void updateBook(Book book) throws BookNotFoundException, PersistenceException {
        try {
            bookDAO.updateBook(book);
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e);
        } catch (PersistenceException e) {
            throw new PersistenceException(e);
        }
    }

    public void deleteBook(Book book) throws BookNotFoundException, PersistenceException {
        try {
            bookDAO.deleteBook(book);
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e);
        } catch (PersistenceException e) {
            throw new PersistenceException(e);
        }
    }

    public int countBooks() {
        int bookCount = 0;

        try {
            bookCount = bookDAO.getAllBooks().size();
        } catch (BookNotFoundException e) {
            bookCount = 0;
        } finally {
            return bookCount;
        }
    }

    public void addBookInstances(BookInstance bookInstance) throws AlreadyExistingBookInstance, PersistenceException {
        try {
            bookDAO.createBookInstance(bookInstance);
        } catch (AlreadyExistingBookInstance e) {
            throw new AlreadyExistingBookInstance(e);
        } catch (PersistenceException e) {
            throw new PersistenceException(e);
        }
    }

    public void deleteBookInstances(BookInstance bookInstance) throws BookInstanceNotFound {
        try {
            bookDAO.deleteBookInstance(bookInstance);
        } catch (BookInstanceNotFound e) {
            throw new BookInstanceNotFound(e);
        }
    }

    public void lendBook(Borrowing borrow) throws NotExistingBorrowingException, PersistenceException {
        BorrowStatus originalStatus = borrow.getStatus();

        try {
            borrow.setStatus(BorrowStatus.BORROWED);
            bookDAO.updateBorrowing(borrow);
        } catch (NotExistingBorrowingException e) {
            borrow.setStatus(originalStatus);
            throw new NotExistingBorrowingException(e);
        }
    }

    public Collection<Borrowing> listBorrowings() throws NoBorrowingsFoundException {
        try {
            return bookDAO.getAllBorrowings();
        } catch (NoBorrowingsFoundException e) {
            throw new NoBorrowingsFoundException(e);
        }
    }

    public Collection<Borrowing> listRequests() throws NotExistingBorrowingException {
        try {
            return bookDAO.getBorrowingsByStatus(BorrowStatus.REQUESTED);
        } catch (NotExistingBorrowingException e) {
            throw new NotExistingBorrowingException(e);
        }
    }

}
