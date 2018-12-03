package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.Genre;
import hu.uni.miskolc.iit.softwaretesting.model.Password;
import hu.uni.miskolc.iit.softwaretesting.model.Reader;
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
import java.util.*;

/**
 * XML DOM implementation of BookDAO interface.
 */
@Repository
public class BookDaoXMLImpl implements BookDAO {

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static BookDaoXMLImpl instance = new BookDaoXMLImpl();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private BookDaoXMLImpl() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static BookDaoXMLImpl getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

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

    public Map<String, String> getReaderCredentials () {
        return this.getCredentialsOfRole("READER");
    }

    public Map<String, String> getLibrarianCredentials () {
        return this.getCredentialsOfRole("LIBRARIAN");
    }

    private Map<String, String> getCredentialsOfRole(String role) {
        NodeList users = document.getElementsByTagName("user");
        Map<String, String> results = new HashMap<>();

        for (int i=0; i < users.getLength(); i++) {
            Element current = (Element) users.item(i);

            String currentRole = this.getNodeValue(current,"role");

            if (currentRole.equals(role)) {
                String username = this.getNodeValue(current, "username");
                String passwordHash = this.getNodeValue(
                        this.getNode(current, "password"),
                        "hashedPassword"
                );

                results.put(username, passwordHash);
            }
        }

        return results;
    }



    /**
     * Returns the reader by username
     * @param username Username of the reader.
     * @return
     * @throws NotExistingReaderException
     */
    public Reader getReaderByUsername (String username) throws NotExistingReaderException {
        NodeList users = document.getElementsByTagName("user");

        for (int i=0; i < users.getLength(); i++) {
            Element current = (Element) users.item(i);
            if (this.getNodeValue(current, "username").equals(username)) {
                return new Reader (
                        this.getNodeValue(current, "username"),
                        new Password("_"),
                        this.getNodeValue(current, "firstname"),
                        this.getNodeValue(current, "lastname"),
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
    public Node getNode (Node node, String nodeName) {
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
    public String getNodeValue (Node node, String nodeName) {
        return getNode(node, nodeName).getTextContent();
    }

    /**
     * Checks if book exists with the given ISBN number.
     * @param isbn ISBN to search for.
     */
    public boolean isBookExists (long isbn) {
        try {
            return this.getBookByISBN(isbn) != null;
        } catch (BookNotFoundException e) {
            return false;
        }
    }

    /**
     * Saves the created DOM to disk.
     * @throws TransformerException
     * @throws IOException
     */
    public void serializeDOM () throws TransformerException, IOException {
        DOMSource source = new DOMSource(this.document);
        FileWriter writer = new FileWriter(this.outputFile);
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
    }
}
