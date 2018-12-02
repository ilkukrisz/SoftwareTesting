package hu.uni.miskolc.iit.softwaretesting.service.impl;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;
import hu.uni.miskolc.iit.softwaretesting.model.BorrowStatus;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Map;

@Service
public class LibrarianBookServiceImpl extends BookServiceImpl implements LibrarianBookService {


    public void addBook(Book book) throws AlreadyExistingBookException, PersistenceException {
        bookDAO.createBook(book);
    }

    public void updateBook(Book book) throws BookNotFoundException, PersistenceException {
        bookDAO.updateBook(book);
    }

    public void deleteBook(Book book) throws BookNotFoundException, PersistenceException {
        bookDAO.deleteBook(book);
    }

    public int countBooks() {
        int bookCount = 0;

        try {
            bookCount = bookDAO.getAllBooks().size();
        } catch (BookNotFoundException e) {
            bookCount = 0;
        }

        return bookCount;
    }

    public void addBookInstances(BookInstance bookInstance) throws AlreadyExistingBookInstanceException, PersistenceException {
        bookDAO.createBookInstance(bookInstance);
    }

    public void deleteBookInstances(BookInstance bookInstance) throws BookInstanceNotFoundException {
        bookDAO.deleteBookInstance(bookInstance);
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
        return bookDAO.getAllBorrowings();
    }

    public Collection<Borrowing> listRequests() throws NotExistingBorrowingException {
        return bookDAO.getBorrowingsByStatus(BorrowStatus.REQUESTED);
    }

    public Map<String, String> getLibrarianCredentials() {
        return bookDAO.getLibrarianCredentials();
    }
}
