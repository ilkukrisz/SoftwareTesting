package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.AlreadyExistingBookInstanceException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.BookInstanceNotFoundException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.BookNotFoundException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.PersistenceException;
import hu.uni.miskolc.iit.softwaretesting.model.Book;
import hu.uni.miskolc.iit.softwaretesting.model.BookInstance;
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
import java.util.ArrayList;

/**
 * XML DOM implementation of BookDAO interface.
 */
@Repository
public class BookInstanceDaoXMLImpl implements BookInstanceDAO {

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static BookInstanceDaoXMLImpl instance = new BookInstanceDaoXMLImpl();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private BookInstanceDaoXMLImpl() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static BookInstanceDaoXMLImpl getInstance() {
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
    public BookInstanceDaoXMLImpl(String inputFile, String outputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.document = dBuilder.parse(new File(inputFile));
        document.getDocumentElement().normalize();
        this.outputFile = new File(outputFile);
        //this.resource = resource;
    }

    /**
     * Creates a new book instance in database.
     *
     * @param bookInstance Data container of the new book instance.
     */
    public void createBookInstance(BookInstance bookInstance) throws AlreadyExistingBookInstanceException, PersistenceException {
        if (this.isBookInstanceExists(bookInstance.getInventoryNumber())) {
            throw new AlreadyExistingBookInstanceException();
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
    public ArrayList<BookInstance> getAllBookInstances() throws BookInstanceNotFoundException {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        ArrayList<BookInstance> results = new ArrayList<>();

        try {
            for (int i=0; i < bookInstances.getLength(); i++) {
                Element current = (Element) bookInstances.item(i);
                results.add(
                        new BookInstance(
                                Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                                BookDaoXMLImpl.getInstance().getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                                Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                        )
                );
            }
        } catch (BookNotFoundException e) {
            throw new BookInstanceNotFoundException(e);
        }

        return results;
    }

    /**
     * Returns all instances of the given book.
     *
     * @param book The book object, which we search instances of.
     */
    public ArrayList<BookInstance> getAllInstancesOfBook(Book book) throws BookNotFoundException, BookInstanceNotFoundException {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        String bookISBN = String.valueOf(book.getIsbn());
        ArrayList<BookInstance> results = new ArrayList<>();

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);
            if (this.getNodeValue(current, "bookISBN").equals(bookISBN)) {
                results.add(
                        new BookInstance(
                                Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                                BookDaoXMLImpl.getInstance().getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                                Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                        )
                );
            }
        }

        if (results.isEmpty()) {
            throw new BookInstanceNotFoundException();
        }

        return results;
    }

    /**
     * Returns all borrowable instances of the given book.
     *
     * @param book The book object, which we search instances of.
     */
    public ArrayList<BookInstance> getAvailableInstancesOfBook(Book book) throws BookNotFoundException, BookInstanceNotFoundException {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");
        String bookISBN = String.valueOf(book.getIsbn());
        ArrayList<BookInstance> results = new ArrayList<>();

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);

            if (this.getNodeValue(current, "bookISBN").equals(bookISBN) &&
                    !Boolean.valueOf(this.getNodeValue(current, "isLoaned"))) {
                results.add(
                        new BookInstance(
                                Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                                BookDaoXMLImpl.getInstance().getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                                Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                        )
                );
            }
        }

        if (results.isEmpty()) {
            throw new BookInstanceNotFoundException();
        }

        return results;
    }

    /**
     * Returns the book instance with the given inventoryNumber.
     *
     * @param inventoryNumber Inventory number of the book instance.
     */
    public BookInstance getBookInstanceByInventoryNumber(long inventoryNumber) throws BookInstanceNotFoundException, BookNotFoundException {
        NodeList bookInstances = document.getElementsByTagName("bookInstance");

        for (int i=0; i < bookInstances.getLength(); i++) {
            Element current = (Element) bookInstances.item(i);
            if (Long.valueOf(this.getNodeValue(current, "inventoryNumber")).equals(inventoryNumber)) {
                return new BookInstance(
                        Long.valueOf(this.getNodeValue(current, "inventoryNumber")),
                        BookDaoXMLImpl.getInstance().getBookByISBN(Long.valueOf(this.getNodeValue(current, "bookISBN"))),
                        Boolean.valueOf(this.getNodeValue(current, "isLoaned"))
                );
            }
        }

        throw new BookInstanceNotFoundException();
    }

    /**
     * Updates the book instance in the database.
     *
     * @param bookInstance Data container of the new book instance.
     *                     InventoryNumber field must be set to the desired book instance's to update.
     */
    public void updateBookInstance(BookInstance bookInstance) throws BookInstanceNotFoundException, PersistenceException {
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
    public void deleteBookInstance(BookInstance bookInstance) throws BookInstanceNotFoundException {
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
     * Checks if book instance exists with the given ISBN number.
     * @param inventoryNo Inventory no. to search for.
     */
    private boolean isBookInstanceExists (long inventoryNo) {
        try {
            return this.getBookInstanceByInventoryNumber(inventoryNo) != null;
        } catch (BookNotFoundException| BookInstanceNotFoundException e) {
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
