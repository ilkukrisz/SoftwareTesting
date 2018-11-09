package hu.uni.miskolc.iit.softwaretesting;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.*;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@Controller
@RequestMapping("/reader")
public class ReaderController {

    private ReaderBookService readerBookService;

    public ReaderController(ReaderBookService readerBookService) { this.readerBookService = readerBookService; }

    @RequestMapping(value = "/booksbyauthor", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BookType> booksByAuthor(@RequestParam(value = "author") String author) throws BookNotFoundException, EmptyFieldException {
        return convertBookToBookType(readerBookService.getBooksByAuthor(author));
    }

    @RequestMapping(value = "/booksbycategory", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BookType> bookByCategory(@RequestParam(value = "category") String category) throws EmptyFieldException, BookNotFoundException, NotExistingGenreException {
        return convertBookToBookType(readerBookService.getBooksByCategory(Genre.valueOf(category)));
    }

    @RequestMapping(value = "/booksbyavailability", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BookType> availableBooks() throws BookNotFoundException {
        return convertBookToBookType(readerBookService.getBooksByAvailability());
    }

    @RequestMapping(value = "/aebooksbytitle", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BookType> availableBooksByTitle(@RequestParam(value = "title") String title) throws BookNotFoundException, EmptyFieldException {
        return convertBookToBookType(readerBookService.getAvailableBooksByTitle(title));
    }

    @RequestMapping(value = "/booksbyyear", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BookType> booksbyYear(@RequestParam(value = "year") int year) throws BookNotFoundException, EmptyFieldException, InvalidPublishDateException, NotExistingGenreException {
        return convertBookToBookType(readerBookService.getBooksByYear(year));
    }

    @RequestMapping(value = "/requestbook", method = RequestMethod.POST)
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
    @ResponseBody
    public Collection<BorrowingType> borrowings(@RequestParam(value = "username") String username) throws NotExistingReaderException, NotExistingBorrowingException {
        Reader reader = new Reader(username, new Password("asd123"), "asd", "123", "asd@asd.hu", "061234567898");
        return convertBorrowingToBorrowingType(readerBookService.showBorrowings(reader));
    }


    @RequestMapping(value = "/allbook", method = RequestMethod.GET)
    @ResponseBody
    public Collection<BookType> allBook() throws BookNotFoundException {
        return convertBookToBookType(readerBookService.getAllBooks());
    }


    private Collection<BookType> convertBookToBookType(Collection<Book> books) {
        Collection<BookType> bookTypeCollection = new ArrayList<>();
        for (Book i : books) {
            BookType bookType = new BookType();
            bookType.setAuthor(i.getAuthor());
            bookType.setGenre(String.valueOf(i.getGenre()));
            bookType.setIsbn(i.getIsbn());
            bookType.setPublishDate(i.getPublishDate());
            bookType.setTitle(i.getTitle());
            bookTypeCollection.add(bookType);
        }
        return bookTypeCollection;
    }

    private Collection<BorrowingType> convertBorrowingToBorrowingType(Collection<Borrowing> borrowings) {
        Collection<BorrowingType> bookTypeCollection = new ArrayList<>();
        for (Borrowing i : borrowings) {
            BorrowingType borrowingType = new BorrowingType();
            borrowingType.setBookInstance(convertBookInstanceToBookInstanceType(i.getBookInstance()));
            borrowingType.setBorrowID(i.getBorrowID());
            borrowingType.setBorrowStatus(String.valueOf(i.getStatus()));
            borrowingType.setReader(convertReaderToUserType(i.getReader()));
            XMLGregorianCalendar ass = new XMLGregorianCalendarImpl();
            ass.setTime(i.getCreationDate().getYear(), i.getCreationDate().getMonth(), i.getCreationDate().getDate());
            borrowingType.setCreationDate(ass);
            ass.setTime(i.getExpirationDate().getYear(), i.getExpirationDate().getMonth(), i.getExpirationDate().getDate());
            borrowingType.setExpirationDate(ass);
            bookTypeCollection.add(borrowingType);
        }
        return bookTypeCollection;
    }

    private BookInstanceType convertBookInstanceToBookInstanceType(BookInstance bookInstance) {
        BookInstanceType bookInstanceType = new BookInstanceType();
        bookInstanceType.setInventoryNumber(bookInstance.getInventoryNumber());
        Collection<Book> books = new ArrayList<>();
        books.add(bookInstance.getBook());
        bookInstanceType.setBook(((List<BookType>) convertBookToBookType(books)).get(0));
        bookInstanceType.setIsLoaned(bookInstance.isLoaned());

        return bookInstanceType;
    }

    private UserType convertReaderToUserType(Reader reader) {
        UserType userType = new UserType();
        userType.setEmail(reader.getEmail());
        userType.setFirstName(reader.getFirstName());
        userType.setLastName(reader.getLastName());
        userType.setMobileNumber(reader.getMobileNumber());
        userType.setUsername(reader.getUsername());
        userType.setPassword(convertPasswordToPasswordType(reader.getPassword()));

        return userType;
    }

    private PasswordType convertPasswordToPasswordType(Password password) {
        PasswordType passwordType = new PasswordType();

        passwordType.setHashedPassword(password.getHashedPassword());
        return passwordType;
    }
}
