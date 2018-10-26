package hu.uni.miskolc.iit.softwaretesting.service;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.Genre;

import java.util.Collection;

public interface BookService {

    /**
     *
     * @return With the books stored in the database.
     */
    public Collection<Book> getAllBooks() throws BookNotFoundException;



}
