package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.converterMethods.ConverterMethods;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.BookInstanceType;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.BookType;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.BorrowingType;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;


@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private LibrarianBookService librarianBookService;

    @Autowired
    public LibrarianController(LibrarianBookService librarianBookService) { this.librarianBookService = librarianBookService; }

    @PostMapping("/addbook")
    @ResponseStatus(HttpStatus.OK)
    public void addBookFromForm(@ModelAttribute("bookType") BookType bookType) throws InvalidPublishDateException, WrongISBNException, PersistenceException, AlreadyExistingBookException, EmptyFieldException, NotExistingGenreException {
        librarianBookService.addBook(ConverterMethods.convertBookTypeToBook(bookType));
    }

    @PostMapping("/updatebook")
    @ResponseStatus(HttpStatus.OK)
    public void updateBookFromForm(@ModelAttribute BookType bookType) throws InvalidPublishDateException, NotExistingGenreException, PersistenceException, WrongISBNException, EmptyFieldException, BookNotFoundException {
        librarianBookService.updateBook(ConverterMethods.convertBookTypeToBook(bookType));
    }

    @PostMapping("/deletebook")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBookFromForm(@ModelAttribute BookType bookType) throws InvalidPublishDateException, PersistenceException, BookNotFoundException {
        librarianBookService.deleteBook(ConverterMethods.convertBookTypeToBook(bookType));
    }

    @RequestMapping(value = "/countbooks", method = RequestMethod.GET)
    @ResponseBody
    public int countBooks() {
        return librarianBookService.countBooks();
    }

    @PostMapping("/addbi")
    @ResponseStatus(HttpStatus.OK)
    public void addBookInstance(@ModelAttribute BookInstanceType bookInstanceType) throws InvalidPublishDateException, AlreadyExistingBookInstance, EmptyFieldException, PersistenceException {
        librarianBookService.addBookInstances(ConverterMethods.convertBookInstanceTypeToBookInstance(bookInstanceType));
    }

    @PostMapping("/deletebi")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBookInstance(@ModelAttribute BookInstanceType bookInstanceType) throws InvalidPublishDateException, BookInstanceNotFound {
        librarianBookService.deleteBookInstances(ConverterMethods.convertBookInstanceTypeToBookInstance(bookInstanceType));
    }

    @PostMapping("/lendbook")
    @ResponseStatus(HttpStatus.OK)
    public void lendBook(@ModelAttribute BorrowingType borrowingType) throws InvalidPublishDateException, PersistenceException, NotExistingBorrowingException {
        librarianBookService.lendBook(ConverterMethods.convertBorrowingTypeToBorrowing(borrowingType));
    }

    @RequestMapping(value = "/listborrowings", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BorrowingType> listBorrowings() {
        try {
            return ConverterMethods.convertBorrowingToBorrowingType(librarianBookService.listBorrowings());
        } catch (NoBorrowingsFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/listrequests", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BorrowingType> listRequests() throws NotExistingBorrowingException {
        return ConverterMethods.convertBorrowingToBorrowingType(librarianBookService.listRequests());
    }
}
