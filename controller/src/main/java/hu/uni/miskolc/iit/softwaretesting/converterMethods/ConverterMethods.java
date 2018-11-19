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
            Calendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(i.getCreationDate());

            ass.setYear(gregorianCalendar.get(Calendar.YEAR));
            ass.setMonth(gregorianCalendar.get(Calendar.MONTH) + 1);
            ass.setDay(gregorianCalendar.get(Calendar.DAY_OF_MONTH));
            borrowingType.setCreationDate(ass);

            gregorianCalendar.setTime(i.getExpirationDate());

            ass.setYear(gregorianCalendar.get(Calendar.YEAR));
            ass.setMonth(gregorianCalendar.get(Calendar.MONTH) + 1);
            ass.setDay(gregorianCalendar.get(Calendar.DAY_OF_MONTH));

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
        int creationYear = (borrowingTypes.getCreationDate() == null) ? 2010 : borrowingTypes.getCreationDate().getYear();
        int creationMonth = (borrowingTypes.getCreationDate() == null) ? 1 : borrowingTypes.getCreationDate().getMonth();
        int creationDay = (borrowingTypes.getCreationDate() == null) ? 1 : borrowingTypes.getCreationDate().getDay();
        int expirationYear = (borrowingTypes.getCreationDate() == null) ? 2010 : borrowingTypes.getCreationDate().getYear();
        int expirationMonth = (borrowingTypes.getExpirationDate() == null) ? 1 : borrowingTypes.getExpirationDate().getMonth();
        int expirationDay = (borrowingTypes.getExpirationDate() == null) ? 2 : borrowingTypes.getExpirationDate().getDay();

        Calendar calendar = Calendar.getInstance();
        calendar.set(creationYear, creationMonth, creationDay);
        Date creationDate = calendar.getTime();
        calendar.set(expirationYear, expirationMonth, expirationDay);
        Date expirationDate = calendar.getTime();

        return new Borrowing(borrowingTypes.getBorrowID(), convertUserTypeToReader(borrowingTypes.getReader()), creationDate, expirationDate, BorrowStatus.valueOf(borrowingTypes.getBorrowStatus()), convertBookInstanceTypeToBookInstance(borrowingTypes.getBookInstance()));
    }

    public static BookInstance convertBookInstanceTypeToBookInstance(BookInstanceType bookInstanceType) throws InvalidPublishDateException {
        ArrayList<BookType> bookType = new ArrayList<>();
        bookType.add(bookInstanceType.getBook());

        return new BookInstance(bookInstanceType.getInventoryNumber(), (Book) convertBookTypeToBook(bookType).toArray()[0], bookInstanceType.isIsLoaned());
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
