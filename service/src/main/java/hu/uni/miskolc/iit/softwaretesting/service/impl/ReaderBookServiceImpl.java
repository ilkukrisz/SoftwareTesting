package hu.uni.miskolc.iit.softwaretesting.service.impl;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ReaderBookServiceImpl extends BookServiceImpl implements ReaderBookService {

    private BookDAO dao;

    public ReaderBookServiceImpl(BookDAO dao) {
        super(dao);
    }

    @Override
    public Collection<Book> getBooksByAuthor(String author) throws EmptyFieldException, BookNotFoundException {

        if (isEmptyField(author))
            throw new EmptyFieldException("The field should not be empty!");

        return dao.getBooksByAuthor(author);
    }

    @Override
    public Collection<Book> getBooksByCategory(Genre genre) throws NotExistingGenreException, BookNotFoundException, EmptyFieldException {

        if (isEmptyField(String.valueOf(genre)))
            throw new EmptyFieldException("The field should not be empty!");

        if (Genre.isContained(String.valueOf(genre)))
            throw new NotExistingGenreException("The given value is not considered as a genre");

        return dao.getBooksByGenre(genre);
    }

    @Override
    public Collection<Book> getBooksByAvailability() throws BookNotFoundException {
        return dao.getAvailableBooks();
    }

    @Override
    public Collection<Book> getAvailableBooksByTitle(String title) throws EmptyFieldException, BookNotFoundException {

        if (isEmptyField(title))
            throw new EmptyFieldException("The given value should not be empty!");

        Collection<Book> results = dao.getAvailableBooks();
        Collection<Book> booksByTitle = dao.getBooksByTitle(title);

        for (Book i : results) {
            for (Book j : booksByTitle) {
                if (i.getIsbn() != j.getIsbn()) {
                    results.remove(j);
                }
            }
        }

        return results;
    }

    @Override
    public Collection<Book> getBooksByYear(int year) throws BookNotFoundException, EmptyFieldException {

        if (isEmptyField(String.valueOf(year)))
            throw new EmptyFieldException("The field should not be empty!");

        return dao.getBooksByYear(year);
    }

    @Override
    public void requestBook(Book book, Reader reader) throws NoAvailableInstanceException, BookNotFoundException {
        try {
            Collection<BookInstance> bookInstances = dao.getAvailableInstancesOfBook(book);
            Calendar creationDate = Calendar.getInstance();
            Calendar expirationDate = Calendar.getInstance();
            expirationDate.setTime(creationDate.getTime());
            expirationDate.add(Calendar.DATE, 30);
            Borrowing borrowing = new Borrowing(dao.getNewID(), reader, creationDate.getTime(), expirationDate.getTime(), BorrowStatus.REQUESTED, ((List<BookInstance>)bookInstances).get(0));
            dao.createBorrowing(borrowing);
        } catch (BookInstanceNotFound bookInstanceNotFound) {
            bookInstanceNotFound.printStackTrace();
            throw new NoAvailableInstanceException("There is no book to be borrowed.");

        } catch (BookNotFoundException e) {
            throw new BookNotFoundException("There is no book that requested.");
        } catch (AlreadyExistingBorrowingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Borrowing> showBorrowings(Reader reader) throws NotExistingReaderException, NotExistingBorrowingException {
        if (reader.equals(null))
            throw new NotExistingReaderException("The given reader is null!");

        if (dao.getBorrowingsOfReader(reader) == null || dao.getBorrowingsOfReader(reader).size() == 0)
            throw new NotExistingBorrowingException("There is no borrowings for this user!");

        return dao.getBorrowingsOfReader(reader);
    }

    @Override
    public Collection<Book> getAllBooks() throws BookNotFoundException {
        return dao.getAllBooks();
    }


    private boolean isEmptyField(String field) {
        if (field.equalsIgnoreCase("") || field.equals(null))
            return true;

        return false;
    }


}
