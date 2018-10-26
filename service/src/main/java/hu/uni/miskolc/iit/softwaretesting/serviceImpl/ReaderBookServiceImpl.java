package hu.uni.miskolc.iit.softwaretesting.serviceImpl;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ReaderBookServiceImpl implements ReaderBookService {

    private BookDAO bookDAO;

    @Override
    public Collection<Book> getBooksByAuthor(String author) throws EmptyFieldException, BookNotFoundException {

        if (isEmptyField(author))
            throw new EmptyFieldException("The field should not be empty!");

        return bookDAO.getBooksByAuthor(author);
    }

    @Override
    public Collection<Book> getBooksByCategory(Genre genre) throws NotExistingGenreException, BookNotFoundException, EmptyFieldException {

        if (isEmptyField(String.valueOf(genre)))
            throw new EmptyFieldException("The field should not be empty!");

        if (Genre.isContained(String.valueOf(genre)))
            throw new NotExistingGenreException("The given value is not considered as a genre");

        return bookDAO.getBooksByGenre(genre);
    }

    @Override
    public Collection<Book> getBooksByAvailability() throws BookNotFoundException {
        return bookDAO.getAvailableBooks();
    }

    @Override
    public Collection<Book> getAvailableBooksByTitle(String title) throws EmptyFieldException, BookNotFoundException {

        if (isEmptyField(title))
            throw new EmptyFieldException("The given value should not be empty!");

        Collection<Book> results = bookDAO.getAvailableBooks();
        Collection<Book> alma = bookDAO.getBooksByTitle(title);

        for (Book i : results) {
            for (Book j : alma) {
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

        return bookDAO.getBooksByYear(year);
    }

    @Override
    public void requestBook(Book book, Reader reader) throws NoAvailableInstanceException {
        try {
            Collection<BookInstance> bookInstances = bookDAO.getAvailableInstancesOfBook(book);
            Calendar creationDate = Calendar.getInstance();
            Calendar expirationDate = Calendar.getInstance();
            expirationDate.setTime(creationDate.getTime());
            expirationDate.add(Calendar.DATE, 30);
            Borrowing borrowing = new Borrowing(createBorrowID(), reader, creationDate.getTime(), expirationDate.getTime(), BorrowStatus.REQUESTED, ((List<BookInstance>)bookInstances).get(0));
            bookDAO.createBorrowing(borrowing);
        } catch (BookInstanceNotFound bookInstanceNotFound) {
            bookInstanceNotFound.printStackTrace();
            throw new NoAvailableInstanceException("There is no book to be borrowed.");

        } catch (AlreadyExistingBorrowingException e) {
            e.printStackTrace();
        } catch (BookNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Borrowing> showBorrowings(Reader reader) throws NotExistingReaderException, NotExistingBorrowingException {
        if (reader.equals(null))
            throw new NotExistingReaderException("The given reader is null!");

        if (bookDAO.getBorrowingsOfReader(reader) == null || bookDAO.getBorrowingsOfReader(reader).size() == 0)
            throw new NotExistingBorrowingException("There is no borrowings for this user!");

        return bookDAO.getBorrowingsOfReader(reader);
    }

    @Override
    public Collection<Book> getAllBooks() throws BookNotFoundException {
        return bookDAO.getAllBooks();
    }


    private boolean isEmptyField(String field) {
        if (field.equalsIgnoreCase("") || field.equals(null))
            return true;

        return false;
    }


    private Long createBorrowID() {
        return (new Random()).nextLong();
    }

}
