package hu.uni.miskolc.iit.softwaretesting.converterMethods;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.*;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

public class ConverterMethods {

    public static Collection<BookType> convertBookToBookType(Collection<Book> books) {
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

    public static Collection<BorrowingType> convertBorrowingToBorrowingType(Collection<Borrowing> borrowings) {
        Collection<BorrowingType> bookTypeCollection = new ArrayList<>();
        for (Borrowing i : borrowings) {
            BorrowingType borrowingType = new BorrowingType();
            borrowingType.setBookInstance(convertBookInstanceToBookInstanceType(i.getBookInstance()));
            borrowingType.setBorrowID(i.getBorrowID());
            borrowingType.setBorrowStatus(String.valueOf(i.getStatus()));
            borrowingType.setReader(convertReaderToUserType(i.getReader()));
            XMLGregorianCalendar ass = new XMLGregorianCalendarImpl();
            // TODO: setTime(int hour, int minute, int second)
            // use setYear, setMonth, setDay instead
            ass.setTime(i.getCreationDate().getYear(), i.getCreationDate().getMonth(), i.getCreationDate().getDate());
            borrowingType.setCreationDate(ass);
            //TODO: setTime(int hour, int minute, int second)
            // use setYear, setMonth, setDay instead
            ass.setTime(i.getExpirationDate().getYear(), i.getExpirationDate().getMonth(), i.getExpirationDate().getDate());
            borrowingType.setExpirationDate(ass);
            bookTypeCollection.add(borrowingType);
        }
        return bookTypeCollection;
    }

    public static BookInstanceType convertBookInstanceToBookInstanceType(BookInstance bookInstance) {
        BookInstanceType bookInstanceType = new BookInstanceType();
        bookInstanceType.setInventoryNumber(bookInstance.getInventoryNumber());
        Collection<Book> books = new ArrayList<>();
        books.add(bookInstance.getBook());
        bookInstanceType.setBook(((List<BookType>) convertBookToBookType(books)).get(0));
        bookInstanceType.setIsLoaned(bookInstance.isLoaned());

        return bookInstanceType;
    }

    public static UserType convertReaderToUserType(Reader reader) {
        UserType userType = new UserType();
        userType.setEmail(reader.getEmail());
        userType.setFirstName(reader.getFirstName());
        userType.setLastName(reader.getLastName());
        userType.setMobileNumber(reader.getMobileNumber());
        userType.setUsername(reader.getUsername());
        userType.setPassword(convertPasswordToPasswordType(reader.getPassword()));

        return userType;
    }

    public static PasswordType convertPasswordToPasswordType(Password password) {
        PasswordType passwordType = new PasswordType();

        passwordType.setHashedPassword(password.getHashedPassword());
        return passwordType;
    }


    public static Collection<Book> convertBookTypeToBook(Collection<BookType> books) throws InvalidPublishDateException {
        Collection<Book> bookArrayList = new ArrayList<>();
        for (BookType i : books) {
            bookArrayList.add(new Book(i.getAuthor(), i.getTitle(), i.getIsbn(), i.getPublishDate(), Genre.valueOf(i.getGenre())));
        }
        return bookArrayList;
    }

    public static Book convertBookTypeToBook(BookType booktype) throws InvalidPublishDateException {
        return new Book(booktype.getAuthor(), booktype.getTitle(), booktype.getIsbn(), booktype.getPublishDate(), Genre.valueOf(booktype.getGenre()));
    }

    public static Collection<Borrowing> convertBorrowingTypeToBorrowing(Collection<BorrowingType> borrowingTypes) throws InvalidPublishDateException {
        Collection<Borrowing> borrowingArrayList = new ArrayList<>();
        for (BorrowingType i : borrowingTypes) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(i.getCreationDate().getYear(), i.getCreationDate().getMonth(), i.getCreationDate().getDay());
            Date creationDate = calendar.getTime();
            calendar.set(i.getCreationDate().getYear(), i.getCreationDate().getMonth(), i.getCreationDate().getDay());
            Date expirationDate = calendar.getTime();
            Borrowing borrowing = new Borrowing(i.getBorrowID(), convertUserTypeToReader(i.getReader()), creationDate, expirationDate, BorrowStatus.valueOf(i.getBorrowStatus()), convertBookInstanceTypeToBookInstance(i.getBookInstance()));
            borrowingArrayList.add(borrowing);
        }
        return borrowingArrayList;
    }

    public static Borrowing convertBorrowingTypeToBorrowing(BorrowingType borrowingTypes) throws InvalidPublishDateException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(borrowingTypes.getCreationDate().getYear(), borrowingTypes.getCreationDate().getMonth(), borrowingTypes.getCreationDate().getDay());
        Date creationDate = calendar.getTime();
        calendar.set(borrowingTypes.getCreationDate().getYear(), borrowingTypes.getCreationDate().getMonth(), borrowingTypes.getCreationDate().getDay());
        Date expirationDate = calendar.getTime();
        return new Borrowing(borrowingTypes.getBorrowID(), convertUserTypeToReader(borrowingTypes.getReader()), creationDate, expirationDate, BorrowStatus.valueOf(borrowingTypes.getBorrowStatus()), convertBookInstanceTypeToBookInstance(borrowingTypes.getBookInstance()));
    }

    public static BookInstance convertBookInstanceTypeToBookInstance(BookInstanceType bookInstanceType) throws InvalidPublishDateException {
        return new BookInstance(bookInstanceType.getInventoryNumber(), (Book) convertBookTypeToBook((Collection<BookType>) bookInstanceType.getBook()), bookInstanceType.isIsLoaned());
    }

    public static Librarian convertUserTypeToLibrarian(UserType userType) {
        return new Librarian(userType.getUsername(), convertPasswordTypeToPassword(userType.getPassword()), userType.getFirstName(), userType.getLastName(), userType.getEmail(), userType.getMobileNumber());
    }

    public static Reader convertUserTypeToReader(UserType userType) {
        return new Reader(userType.getUsername(), convertPasswordTypeToPassword(userType.getPassword()), userType.getFirstName(), userType.getLastName(), userType.getEmail(), userType.getMobileNumber());
    }

    public static Password convertPasswordTypeToPassword(PasswordType passwordType) {
        return new Password(passwordType.getHashedPassword());
    }















}
