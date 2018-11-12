package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.impl.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * XML DOM implementation of BookDAO interface.
 */
@Repository
public class BookDaoXMLImpl implements BookDAO {

    /**
     * The document to manipulate.
     */
    private Document document;

    /**
     * The file to write out data changes. It can be the same as document input file.
     */
    private File outputFile;

    @Autowired
    public BookDaoXMLImpl(String inputFile, String outputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.document = dBuilder.parse(new File(inputFile));
        document.getDocumentElement().normalize();
        this.outputFile = new File(outputFile);
        //this.resource = resource;
    }

    /**
     * Creates a new book record in database.
     *
     * @param book Data container of the new book.
     */
    public void createBook(Book book) throws AlreadyExistingBookException, PersistenceException {
        if (this.isBookExists(book.getIsbn())) {
            throw new AlreadyExistingBookException();
        }

        try {
            Element newBook = document.createElement("book");

            Element ISBN = document.createElement("ISBN");
            ISBN.appendChild(document.createTextNode(String.valueOf(book.getIsbn())));
            newBook.appendChild(ISBN);

            Element author = document.createElement("author");
            author.appendChild(document.createTextNode(book.getAuthor()));
            newBook.appendChild(author);

            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(book.getTitle()));
            newBook.appendChild(title);

            Element publishDate = document.createElement("publishDate");
            publishDate.appendChild(document.createTextNode(String.valueOf(book.getPublishDate())));
            newBook.appendChild(publishDate);

            Element genre = document.createElement("genre");
            genre.appendChild(document.createTextNode(book.getGenre().toString()));
            newBook.appendChild(genre);

            document.getElementsByTagName("books").item(0).appendChild(newBook);
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Returns all books from database.
     */
    public ArrayList<Book> getAllBooks() throws BookNotFoundException {
        NodeList books = document.getElementsByTagName("book");
        ArrayList<Book> results = new ArrayList<>();
        long ISBN = 0;

        try {
            for (int i=0; i < books.getLength(); i++) {
                Element current = (Element) books.item(i);

                ISBN = Long.valueOf(this.getNodeValue(current, "ISBN"));
                String author = this.getNodeValue(current, "author");
                String title = this.getNodeValue(current, "title");
                int publishDate = Integer.valueOf(this.getNodeValue(current, "publishDate"));
                Genre genre = Genre.valueOf(this.getNodeValue(current, "genre"));

                results.add(new Book(author, title, ISBN, publishDate, genre));
            }
        } catch (InvalidPublishDateException e) {
            System.err.println("Invalid publish date in database, Book ISBN:" + String.valueOf(ISBN));
        }

        if (results.isEmpty()) {
            throw new BookNotFoundException();
        }

        return results;
}

    /**
     * Returns all borrowable books.
     */
    public ArrayList<Book> getAvailableBooks() throws BookNotFoundException {
        NodeList books = document.getElementsByTagName("book");
        NodeList instances = document.getElementsByTagName("bookInstance");
        ArrayList<Book> results = new ArrayList<Book>();

        try {
            for (int i=0; i < books.getLength(); i++) {
                Element currentBook = (Element) books.item(i);

                for (int j = 0; j < instances.getLength(); j++) {
                    Element currentInstance = (Element) instances.item(i);
                    boolean isLoaned = Boolean.valueOf(this.getNodeValue(currentInstance, "isLoaned"));
                    if (!isLoaned) {
                        results.add(
                                new Book(
                                        this.getNodeValue(currentBook, "author"),
                                        this.getNodeValue(currentBook, "title"),
                                        Long.valueOf(this.getNodeValue(currentBook, "ISBN")),
                                        Integer.valueOf(this.getNodeValue(currentBook, "publishDate")),
                                        Genre.valueOf(this.getNodeValue(currentBook, "genre"))
                                )
                        );
                    }
                } //instances loop
            } //books loop
        } catch (InvalidPublishDateException e) {
            throw new BookNotFoundException(e);
        }

        if (results.isEmpty()) {
            throw new BookNotFoundException();
        }

        return results;
    }

    /**
     * Returns all books with the given title.
     *
     * @param searchedTitle Title of the book.
     */
    public ArrayList<Book> getBooksByTitle(String searchedTitle) throws BookNotFoundException {
        NodeList books = document.getElementsByTagName("book");
        ArrayList<Book> results = new ArrayList<>();
        try {
            for (int i=0; i < books.getLength(); i++) {
                Element current = (Element) books.item(i);
                String currentTitle = getNodeValue(current, "title");

                if (searchedTitle.equals(currentTitle)) {
                    results.add(new Book(
                            this.getNodeValue(current, "author"),
                            this.getNodeValue(current, "title"),
                            Long.valueOf(this.getNodeValue(current, "ISBN")),
                            Integer.valueOf(this.getNodeValue(current, "publishDate")),
                            Genre.valueOf(this.getNodeValue(current, "genre"))
                    ));
                }
            }
        } catch (InvalidPublishDateException e) {
            System.err.println("Invalid publish date in database");
        }

        if (results.isEmpty()) {
            throw new BookNotFoundException("No books found with the given title.");
        }

        return results;
    }

    /**
     * Returns the book with the given ISBN.
     *
     * @param ISBN ISBN number of the searched book.
     */
    public Book getBookByISBN(long ISBN) throws BookNotFoundException {
        String searchedISBN = String.valueOf(ISBN);
        NodeList books = document.getElementsByTagName("book");

        try {
            for (int i=0; i < books.getLength(); i++) {
                Element current = (Element) books.item(i);
                String currentISBN = this.getNodeValue(current, "ISBN");
                if (currentISBN.equals(searchedISBN)) {
                    return new Book(
                            this.getNodeValue(current, "author"),
                            this.getNodeValue(current, "title"),
                            Long.valueOf(this.getNodeValue(current, "ISBN")),
                            Integer.valueOf(this.getNodeValue(current, "publishDate")),
                            Genre.valueOf(this.getNodeValue(current, "genre"))
                    );
                }
            }
        } catch (InvalidPublishDateException e) {
            System.err.println("Invalid publish date in database");
        }

        throw new BookNotFoundException("No books found with the given ISBN.");
    }

    /**
     * Returns the book with the given ISBN.
     *
     * @param searchedAuthor The author of the book.
     */
    public ArrayList<Book> getBooksByAuthor(String searchedAuthor) throws BookNotFoundException {
        NodeList books = document.getElementsByTagName("book");
        ArrayList<Book> results = new ArrayList<>();
        try {
            for (int i=0; i < books.getLength(); i++) {
                Element current = (Element) books.item(i);
                String currentAuthor = getNodeValue(current, "author");

                if (searchedAuthor.equals(currentAuthor)) {
                    results.add(new Book(
                            this.getNodeValue(current, "author"),
                            this.getNodeValue(current, "title"),
                            Long.valueOf(this.getNodeValue(current, "ISBN")),
                            Integer.valueOf(this.getNodeValue(current, "publishDate")),
                            Genre.valueOf(this.getNodeValue(current, "genre"))
                    ));
                }
            }
        } catch (InvalidPublishDateException e) {
            System.err.println("Invalid publish date in database");
        }

        if (results.isEmpty()) {
            throw new BookNotFoundException("No books found with the given title.");
        }

        return results;
    }

    /**
     * Updates the book with the given information.
     *
     * @param book Data container of the new book data. ISBN field have to contain
     *             the desired book's unique ISBN number.
     */
    public void updateBook(Book book) throws BookNotFoundException, PersistenceException {
        NodeList books = document.getElementsByTagName("book");

        for (int i=0; i < books.getLength(); i++) {
            Element current = (Element) books.item(i);
            if (this.getNodeValue(current, "ISBN").equals(String.valueOf(book.getIsbn()))) {
                this.getNode(current, "author").setTextContent(book.getAuthor());
                this.getNode(current, "title").setTextContent(book.getTitle());
                this.getNode(current, "publishDate").setTextContent(String.valueOf(book.getPublishDate()));
            }
        }

        try {
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException("Unable to save DOM", e);
        }
    }

    /**
     * Deletes the book.
     *
     * @param book The book object. ISBN field have to contain the erasable book's unique ISBN number.
     */
    public void deleteBook(Book book) throws BookNotFoundException, PersistenceException {
        NodeList books = document.getElementsByTagName("book");
        String bookID = String.valueOf(book.getIsbn());

        for (int i=0; i < books.getLength(); i++) {
            Element current = (Element) books.item(i);
            if (this.getNodeValue(current, "ISBN").equals(bookID)) {
                current.getParentNode().removeChild(current);
            }
        }

        try {
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException("Unable to save DOM", e);
        }
    }

    /**
     * Creates a new book instance in database.
     *
     * @param bookInstance Data container of the new book instance.
     */
    public void createBookInstance(BookInstance bookInstance) throws AlreadyExistingBookInstance, PersistenceException {
        if (this.isBookInstanceExists(bookInstance.getInventoryNumber())) {
            throw new AlreadyExistingBookInstance();
        }

        try {
            Element newBookInstance = document.createElement("bookInstance");

            Element inventoryNumber = document.createElement("inventoryNumber");
            inventoryNumber.appendChild(document.createTextNode(String.valueOf(bookInstance.getInventoryNumber())));
            newBookInstance.appendChild(inventoryNumber);

            Element bookISBN = document.createElement("bookISBN");
            bookISBN.appendChild(document.createTextNode(String.valueOf(bookInstance.getBook().getIsbn())));
            newBookInstance.appendChild(bookISBN);

            Element isLoaned = document.createElement("isLoaned");
            isLoaned.appendChild(document.createTextNode(Boolean.toString(bookInstance.isLoaned())));
            newBookInstance.appendChild(isLoaned);

            document.getElementsByTagName("bookInstances").item(0).appendChild(newBookInstance);
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException("Failed to create new book instance", e);
        }
    }

    /**
     * Returns all book instances from the database.
     */
    public ArrayList<BookInstance> getAllBookInstances() throws BookInstanceNotFound {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        ArrayList<BookInstance> results = new ArrayList<>();

        try {
            for (int i=0; i < bookInstances.getLength(); i++) {
                Element current = (Element) bookInstances.item(i);
                results.add(
                        new BookInstance(
                                Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                                this.getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                                Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                        )
                );
            }
        } catch (BookNotFoundException e) {
            throw new BookInstanceNotFound(e);
        }

        return results;
    }

    /**
     * Returns all instances of the given book.
     *
     * @param book The book object, which we search instances of.
     */
    public ArrayList<BookInstance> getAllInstancesOfBook(Book book) throws BookNotFoundException, BookInstanceNotFound {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        String bookISBN = String.valueOf(book.getIsbn());
        ArrayList<BookInstance> results = new ArrayList<>();

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);
            if (this.getNodeValue(current, "ISBN").equals(bookISBN)) {
                results.add(
                        new BookInstance(
                                Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                                this.getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                                Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                        )
                );
            }
        }

        if (results.isEmpty()) {
            throw new BookInstanceNotFound();
        }

        return results;
    }

    /**
     * Returns all borrowable instances of the given book.
     *
     * @param book The book object, which we search instances of.
     */
    public ArrayList<BookInstance> getAvailableInstancesOfBook(Book book) throws BookNotFoundException, BookInstanceNotFound {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        String bookISBN = String.valueOf(book.getIsbn());
        ArrayList<BookInstance> results = new ArrayList<>();

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);

            if (this.getNodeValue(current, "ISBN").equals(bookISBN) &&
                    !Boolean.valueOf(this.getNodeValue(current, "isLoaned"))) {
                results.add(
                        new BookInstance(
                                Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                                this.getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                                Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                        )
                );
            }
        }

        if (results.isEmpty()) {
            throw new BookInstanceNotFound();
        }

        return results;
    }

    /**
     * Returns the book instance with the given inventoryNumber.
     *
     * @param inventoryNumber Inventory number of the book instance.
     */
    public BookInstance getBookInstanceByInventoryNumber(long inventoryNumber) throws BookInstanceNotFound, BookNotFoundException {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);
            if (Long.valueOf(this.getNodeValue(current, "inventoryNumber")).equals(inventoryNumber)) {
                return new BookInstance(
                        Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                        this.getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                        Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                );
            }
        }

        throw new BookInstanceNotFound();
    }

    /**
     * Updates the book instance in the database.
     *
     * @param bookInstance Data container of the new book instance.
     *                     InventoryNumber field must be set to the desired book instance's to update.
     */
    public void updateBookInstance(BookInstance bookInstance) throws BookInstanceNotFound, PersistenceException {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);
            if (this.getNodeValue(current, "inventoryNumber").equals(String.valueOf(bookInstance.getInventoryNumber()))) {
                this.getNode(current, "bookISBN").setTextContent(String.valueOf(bookInstance.getBook().getIsbn()));
                this.getNode(current, "isLoaned").setTextContent(Boolean.toString(bookInstance.isLoaned()));
            }
        }

        try {
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Deletes the book instance from the database.
     *
     * @param bookInstance BookInstance object with the eraseable book instance's inventory number.
     */
    public void deleteBookInstance(BookInstance bookInstance) throws BookInstanceNotFound {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        String inventoryNo = String.valueOf(bookInstance.getInventoryNumber());

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);
            if (this.getNodeValue(current, "inventoryNumber").equals(inventoryNo)) {
                current.getParentNode().removeChild(current);
            }
        }
    }

    /**
     * Creates a new borrowing in the database.
     *
     * @param borrowing Data container of the new borrowing.
     */
    public void createBorrowing(Borrowing borrowing) throws AlreadyExistingBorrowingException, PersistenceException {
        if (this.isBorrowingExists(borrowing.getBorrowID())) {
            throw new AlreadyExistingBorrowingException();
        }

        try {
            Element newBorrowing = document.createElement("borrowing");

            Element readerUsername = document.createElement("readerUsername");
            readerUsername.appendChild(document.createTextNode(borrowing.getReader().getUsername()));
            newBorrowing.appendChild(readerUsername);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Element creationDate = document.createElement("creationDate");
            creationDate.appendChild(document.createTextNode(dateFormat.format(borrowing.getCreationDate())));
            newBorrowing.appendChild(creationDate);

            Element expirationDate = document.createElement("expirationDate");
            expirationDate.appendChild(document.createTextNode(dateFormat.format(borrowing.getExpirationDate())));
            newBorrowing.appendChild(expirationDate);

            Element status = document.createElement("status");
            status.appendChild(document.createTextNode(borrowing.getStatus().toString()));
            newBorrowing.appendChild(status);

            document.getElementsByTagName("borrowings").item(0).appendChild(newBorrowing);
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Returns all of the borrowings from the database.
     */
    public ArrayList<Borrowing> getAllBorrowings() throws NoBorrowingsFoundException {
        NodeList borrowings = document.getElementsByTagName("borrowing");
        ArrayList<Borrowing> results = new ArrayList<>();

        try {
            for(int i=0; i < borrowings.getLength(); i++) {
                Element current = (Element) borrowings.item(i);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                results.add(
                        new Borrowing(
                                Long.valueOf(this.getNodeValue(current, "borrowID")),
                                this.getReaderByUsername(this.getNodeValue(current, "readerUsername")),
                                format.parse(this.getNodeValue(current, "creationDate")),
                                format.parse(this.getNodeValue(current, "expirationDate")),
                                BorrowStatus.valueOf(this.getNodeValue(current, "status")),
                                this.getBookInstanceByInventoryNumber(
                                        Long.valueOf(this.getNodeValue(current, "bookInstanceInventoryNumber"))
                                )
                        )
                );
            }
        } catch (ParseException|BookInstanceNotFound|BookNotFoundException|NotExistingReaderException e) {
            throw new NoBorrowingsFoundException(e);
        }

        return results;
    }

    /**
     * Returns all borrowings with the given status.
     *
     * @param status The searched borrowing status.
     */
    public Collection<Borrowing> getBorrowingsByStatus(BorrowStatus status) throws NotExistingBorrowingException {
        NodeList borrowings = document.getElementsByTagName("borrowing");
        ArrayList<Borrowing> results = new ArrayList<>();
        String searchedStatus = status.toString();

        try {
            for (int i=0; i < borrowings.getLength(); i++) {
                Element current = (Element) borrowings.item(i);
                if (this.getNodeValue(current, "status").equals(searchedStatus)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    results.add(
                            new Borrowing(
                                    Long.valueOf(this.getNodeValue(current, "borrowID")),
                                    this.getReaderByUsername(this.getNodeValue(current, "readerUsername")),
                                    format.parse(this.getNodeValue(current, "creationDate")),
                                    format.parse(this.getNodeValue(current, "expirationDate")),
                                    BorrowStatus.valueOf(this.getNodeValue(current, "status")),
                                    this.getBookInstanceByInventoryNumber(
                                            Long.valueOf(this.getNodeValue(current, "bookInstanceInventoryNumber"))
                                    )
                            )
                    );
                }
            }
        } catch (ParseException|BookInstanceNotFound|BookNotFoundException|NotExistingReaderException e) {
            try {
                results.add(new Borrowing(1111111, new Reader("asd", new Password(""), "asd", "asdddd", "asd@ddd.hu", "06506354253"),
                        new Date(), new Date(), BorrowStatus.REQUESTED, new BookInstance(222222, new Book("alma", "almagyozedelmeskedik", 22223123, 2009, Genre.Crimi), false)));
            } catch (InvalidPublishDateException e1) {
                e1.printStackTrace();
            }
            throw new NotExistingBorrowingException(e);
        }

        return results;
    }

    /**
     * Returns all borrowings of the given reader.
     *
     * @param reader The searched reader.
     */
    public Collection<Borrowing> getBorrowingsOfReader(Reader reader) throws NotExistingBorrowingException, NotExistingReaderException {
        NodeList borrowings = document.getElementsByTagName("borrowing");
        ArrayList<Borrowing> results = new ArrayList<>();
        String searchedReaderUsername = reader.getUsername();

        try {
            for (int i=0; i < borrowings.getLength(); i++) {
                Element current = (Element) borrowings.item(i);
                if (this.getNodeValue(current, "readerUsername").equals(searchedReaderUsername)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    results.add(
                            new Borrowing(
                                    Long.valueOf(this.getNodeValue(current, "borrowID")),
                                    this.getReaderByUsername(this.getNodeValue(current, "readerUsername")),
                                    format.parse(this.getNodeValue(current, "creationDate")),
                                    format.parse(this.getNodeValue(current, "expirationDate")),
                                    BorrowStatus.valueOf(this.getNodeValue(current, "status")),
                                    this.getBookInstanceByInventoryNumber(
                                            Long.valueOf(this.getNodeValue(current, "bookInstanceInventoryNumber"))
                                    )
                            )
                    );
                }
            }
        } catch (ParseException|BookInstanceNotFound|BookNotFoundException e) {
            throw new NotExistingBorrowingException(e);
        }

        return results;
    }

    /**
     * Returns the borrowing with the given id.
     * @param id ID to search.
     * @return Borrowing with the given id.
     * @throws NotExistingBorrowingException
     */
    public Borrowing getBorrowingById(long id) throws NotExistingBorrowingException {
        NodeList borrowings = document.getElementsByTagName("borrowing");
        try {
            for (int i=0; i < borrowings.getLength(); i++) {
                Element current = (Element) borrowings.item(i);
                if (this.getNodeValue(current, "borrowID").equals(id)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    return new Borrowing(
                            Long.valueOf(this.getNodeValue(current, "borrowID")),
                            this.getReaderByUsername(this.getNodeValue(current, "readerUsername")),
                            format.parse(this.getNodeValue(current, "creationDate")),
                            format.parse(this.getNodeValue(current, "expirationDate")),
                            BorrowStatus.valueOf(this.getNodeValue(current, "status")),
                            this.getBookInstanceByInventoryNumber(
                                    Long.valueOf(this.getNodeValue(current, "bookInstanceInventoryNumber"))
                            )
                    );
                }
            }
        } catch (ParseException|BookInstanceNotFound|BookNotFoundException|NotExistingReaderException e) {
            throw new NotExistingBorrowingException(e);
        }

        throw new NotExistingBorrowingException();
    }

    /**
     * Updates borrowing in the database.
     *
     * @param borrowing The borrowing object to update.
     *                  BorrowID field have to contain the unique id of the borrowing to update.
     */
    public void updateBorrowing(Borrowing borrowing) throws NotExistingBorrowingException, PersistenceException {
        NodeList borrowings = document.getElementsByTagName("borrowing");

        for (int i=0; i < borrowings.getLength(); i++) {
            Element current = (Element) borrowings.item(i);
            if (this.getNodeValue(current, "borrowID").equals(String.valueOf(borrowing.getBorrowID()))) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                this.getNode(current, "readerUsername").setTextContent(borrowing.getReader().getUsername());
                this.getNode(current, "creationDate").setTextContent(format.format(borrowing.getCreationDate()));
                this.getNode(current, "expirationDate").setTextContent(format.format(borrowing.getExpirationDate()));
                this.getNode(current, "status").setTextContent(borrowing.getStatus().toString());
            }
        }

        try {
            this.serializeDOM();
        } catch (TransformerException|IOException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Deletes the given borrowing from the database.
     *
     * @param borrowing The borrowing object to delete.
     *                  BorrowID field have to contain the unique id of the borrowing to delete.
     */
    public void deleteBorrowing(Borrowing borrowing) throws NotExistingBorrowingException {
        NodeList borrowings = document.getElementsByTagName("borrowing");
        String borrowID = String.valueOf(borrowing.getBorrowID());

        for (int i=0; i < borrowings.getLength(); i++) {
            Element current = (Element) borrowings.item(i);
            if (this.getNodeValue(current, "borrowID").equals(borrowID)) {
                current.getParentNode().removeChild(current);
                return;
            }
        }

        throw new NotExistingBorrowingException();
    }

    /**
     * Returns the books with the given genre.
     *
     * @param genre The genre of the book.
     */
    public ArrayList<Book> getBooksByGenre(Genre genre) throws BookNotFoundException {
        NodeList books = document.getElementsByTagName("book");
        ArrayList<Book> results = new ArrayList<>();

        for (int i=0; i < books.getLength(); i++) {
            Element current = (Element) books.item(i);
            if (this.getNodeValue(current, "genre").equals(genre.toString())) {
                try {
                    results.add(
                            new Book (
                                    this.getNodeValue(current, "author"),
                                    this.getNodeValue(current, "title"),
                                    Long.valueOf(this.getNodeValue(current, "ISBN")),
                                    Integer.valueOf(this.getNodeValue(current, "publishDate")),
                                    Genre.valueOf(this.getNodeValue(current, "genre"))
                            )
                    );
                } catch (InvalidPublishDateException e) {
                    throw new BookNotFoundException(e);
                }
            }
        }

        if (results.isEmpty()) {
            throw new BookNotFoundException();
        }

        return results;
    }

    /**
     * Returns the books with the given publish date.
     *
     * @param year The publish date of the books.
     */
    public Collection<Book> getBooksByYear(int year) throws BookNotFoundException {
        NodeList books = document.getElementsByTagName("book");
        ArrayList<Book> results = new ArrayList<>();

        for (int i=0; i < books.getLength(); i++) {
            Element current = (Element) books.item(i);
            if (this.getNodeValue(current, "publishDate").equals(String.valueOf(year))) {
                try {
                    results.add(
                            new Book (
                                    this.getNodeValue(current, "author"),
                                    this.getNodeValue(current, "title"),
                                    Long.valueOf(this.getNodeValue(current, "ISBN")),
                                    Integer.valueOf(this.getNodeValue(current, "publishDate")),
                                    Genre.valueOf(this.getNodeValue(current, "genre"))
                            )
                    );
                } catch (InvalidPublishDateException e) {
                    throw new BookNotFoundException(e);
                }
            }
        }

        if (results.isEmpty()) {
            throw new BookNotFoundException();
        }

        return results;
    }

    /**
     * Returns an id which is not used yet.
     */
    public long getNewID() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int secs = cal.get(Calendar.SECOND);
        int millisecs = cal.get(Calendar.MILLISECOND);

        return year + month + day + hour + min + secs + millisecs;
    }

    /**
     * Returns the reader by username
     * @param username Username of the reader.
     * @return
     * @throws NotExistingReaderException
     */
    private Reader getReaderByUsername (String username) throws NotExistingReaderException {
        NodeList users = document.getElementsByTagName("users");

        for (int i=0; i < users.getLength(); i++) {
            Element current = (Element) users.item(i);
            if (this.getNodeValue(current, "username").equals(username)) {
                return new Reader (
                        this.getNodeValue(current, "username"),
                        new Password(""),
                        this.getNodeValue(current, "firstName"),
                        this.getNodeValue(current, "lastName"),
                        this.getNodeValue(current, "email"),
                        this.getNodeValue(current, "mobileNumber")
                );
            }
        }

        throw new NotExistingReaderException();
    }

    /**
     * Returns subnode with the given name of the given node.
     * @param node Root node.
     * @param nodeName The searched node's name.
     */
    private Node getNode (Node node, String nodeName) {
        NodeList nodes = node.getChildNodes();
        for (int i=0; i < nodes.getLength(); i++) {
            Node current = nodes.item(i);
            if (current.getNodeName().equals(nodeName)) {
                return current;
            }
        }

        return null;
    }

    /**
     * Returns subnode value with the given name of the given node.
     * @param node Root node
     * @param nodeName The searched node's name
     */
    private String getNodeValue (Node node, String nodeName) {
        return getNode(node, nodeName).getTextContent();
    }

    /**
     * Checks if book exists with the given ISBN number.
     * @param isbn ISBN to search for.
     */
    private boolean isBookExists (long isbn) {
        try {
            return this.getBookByISBN(isbn) != null;
        } catch (BookNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks if book instance exists with the given ISBN number.
     * @param inventoryNo Inventory no. to search for.
     */
    private boolean isBookInstanceExists (long inventoryNo) {
        try {
            return this.getBookInstanceByInventoryNumber(inventoryNo) != null;
        } catch (BookNotFoundException|BookInstanceNotFound e) {
            return false;
        }
    }

    /**
     * Checks if borrowing exists with the given ISBN number.
     * @param borrowID ID to search for.
     */
    private boolean isBorrowingExists (long borrowID) {
        try {
            return this.getBorrowingById(borrowID) != null;
        } catch (NotExistingBorrowingException e) {
            return false;
        }
    }

    /**
     * Saves the created DOM to disk.
     * @throws TransformerException
     * @throws IOException
     */
    private void serializeDOM () throws TransformerException, IOException {
        DOMSource source = new DOMSource(this.document);
        FileWriter writer = new FileWriter(this.outputFile);
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
    }
}
