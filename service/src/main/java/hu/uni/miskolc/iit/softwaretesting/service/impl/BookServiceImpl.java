package hu.uni.miskolc.iit.softwaretesting.service.impl;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BookInstanceDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BorrowingDAO;
import hu.uni.miskolc.iit.softwaretesting.exceptions.BookNotFoundException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookDAO bookDAO;
    BookInstanceDAO bookInstanceDAO;
    BorrowingDAO borrowingDAO;


    public Collection<Book> getAllBooks() throws BookNotFoundException {
        return bookDAO.getAllBooks();
    }
}