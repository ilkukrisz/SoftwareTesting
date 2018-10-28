package hu.uni.miskolc.iit.softwaretesting.service.impl;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.BookNotFoundException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.service.BookService;

import java.util.Collection;

public class BookServiceImpl implements BookService {
    BookDAO dao;

    public BookServiceImpl(BookDAO dao) {
        this.dao = dao;
    }

    public Collection<Book> getAllBooks() throws BookNotFoundException {
        try {
            return dao.getAllBooks();
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(e);
        }
    }
}