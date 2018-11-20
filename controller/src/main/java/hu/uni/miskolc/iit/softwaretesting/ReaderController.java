package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.converterMethods.ConverterMethods;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.*;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/reader")
public class ReaderController {

    private ReaderBookService readerBookService;

    @Autowired
    public ReaderController(ReaderBookService readerBookService) { this.readerBookService = readerBookService; }

    @RequestMapping(value = "/booksbyauthor", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BookType> booksByAuthor(@RequestParam(value = "author") String author) throws BookNotFoundException, EmptyFieldException {
        return ConverterMethods.convertBookToBookType(readerBookService.getBooksByAuthor(author));
    }

    @RequestMapping(value = "/booksbycategory", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BookType> bookByCategory(@RequestParam(value = "category") String category) throws EmptyFieldException, BookNotFoundException, NotExistingGenreException {
        return ConverterMethods.convertBookToBookType(readerBookService.getBooksByCategory(Genre.valueOf(category)));
    }

    @RequestMapping(value = "/booksbyavailability", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BookType> availableBooks() throws BookNotFoundException {
        return ConverterMethods.convertBookToBookType(readerBookService.getBooksByAvailability());
    }

    @RequestMapping(value = "/aebooksbytitle", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BookType> availableBooksByTitle(@RequestParam(value = "title") String title) throws BookNotFoundException, EmptyFieldException {
        return ConverterMethods.convertBookToBookType(readerBookService.getAvailableBooksByTitle(title));
    }

    @RequestMapping(value = "/booksbyyear", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BookType> booksbyYear(@RequestParam(value = "year") int year) throws BookNotFoundException, EmptyFieldException, InvalidPublishDateException, NotExistingGenreException {
        return ConverterMethods.convertBookToBookType(readerBookService.getBooksByYear(year));
    }

    @RequestMapping(value = "/requestbook", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public void requestBook(@RequestParam("isbn") long isbn, @RequestParam("username") String username) throws BookNotFoundException, NoAvailableInstanceException, PersistenceException {
        Book requestedBook = null;
        for (Book i : readerBookService.getAllBooks()) {
            if (i.getIsbn() == isbn)
                requestedBook = i;
        }

        Reader reader = new Reader(username, new Password("asd123"), "asd", "123", "asd@asd.hu", "061234567898");
        readerBookService.requestBook(requestedBook, reader);
    }

    @RequestMapping(value = "/borrowings", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BorrowingType> borrowings(@RequestParam(value = "username") String username) throws NotExistingReaderException, NotExistingBorrowingException {
        Reader reader = new Reader(username, new Password("asd123"), "asd", "123", "asd@asd.hu", "061234567898");
        return ConverterMethods.convertBorrowingToBorrowingType(readerBookService.showBorrowings(reader));
    }


    @RequestMapping(value = "/allbook", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<BookType> allBook() throws BookNotFoundException {
        return ConverterMethods.convertBookToBookType(readerBookService.getAllBooks());
    }
}
