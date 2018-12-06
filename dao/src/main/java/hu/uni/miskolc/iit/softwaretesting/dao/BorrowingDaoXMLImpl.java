package hu.uni.miskolc.iit.softwaretesting.dao;

import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.BorrowStatus;
import hu.uni.miskolc.iit.softwaretesting.model.Borrowing;
import hu.uni.miskolc.iit.softwaretesting.model.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.Collection;

/**
 * XML DOM implementation of BookDAO interface.
 */
@Repository
public class BorrowingDaoXMLImpl implements BorrowingDAO {

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static BorrowingDaoXMLImpl instance = new BorrowingDaoXMLImpl();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private BorrowingDaoXMLImpl() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static BorrowingDaoXMLImpl getInstance() {
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
    public BorrowingDaoXMLImpl(String inputFile, String outputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.document = dBuilder.parse(new File(inputFile));
        document.getDocumentElement().normalize();
        this.outputFile = new File(outputFile);
        //this.resource = resource;
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

            Element borrowID = document.createElement("borrowID");
            borrowID.appendChild(document.createTextNode(String.valueOf(borrowing.getBorrowID())));
            newBorrowing.appendChild(borrowID);

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

            Element bookInstanceInventoryNo = document.createElement("bookInstanceInventoryNumber");
            bookInstanceInventoryNo.appendChild(document.createTextNode(
                    String.valueOf(borrowing.getBookInstance().getInventoryNumber())
            ));
            newBorrowing.appendChild(bookInstanceInventoryNo);

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
                                Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID")),
                                BookDaoXMLImpl.getInstance().getReaderByUsername(BookDaoXMLImpl.getInstance().getNodeValue(current, "readerUsername")),
                                format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "creationDate")),
                                format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "expirationDate")),
                                BorrowStatus.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "status")),
                                BookInstanceDaoXMLImpl.getInstance().getBookInstanceByInventoryNumber(
                                        Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "bookInstanceInventoryNumber"))
                                )
                        )
                );
            }
        } catch (ParseException| BookInstanceNotFoundException |BookNotFoundException|NotExistingReaderException e) {
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
                if (BookDaoXMLImpl.getInstance().getNodeValue(current, "status").equals(searchedStatus)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    results.add(
                            new Borrowing(
                                    Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID")),
                                    BookDaoXMLImpl.getInstance().getReaderByUsername(BookDaoXMLImpl.getInstance().getNodeValue(current, "readerUsername")),
                                    format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "creationDate")),
                                    format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "expirationDate")),
                                    BorrowStatus.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "status")),
                                    BookInstanceDaoXMLImpl.getInstance().getBookInstanceByInventoryNumber(
                                            Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "bookInstanceInventoryNumber"))
                                    )
                            )
                    );
                }
            }
        } catch (ParseException| BookInstanceNotFoundException |BookNotFoundException|NotExistingReaderException e) {
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
        BookDaoXMLImpl.getInstance().getReaderByUsername(searchedReaderUsername);

        try {
            for (int i=0; i < borrowings.getLength(); i++) {
                Element current = (Element) borrowings.item(i);
                if (BookDaoXMLImpl.getInstance().getNodeValue(current, "readerUsername").equals(searchedReaderUsername)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    results.add(
                            new Borrowing(
                                    Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID")),
                                    BookDaoXMLImpl.getInstance().getReaderByUsername(BookDaoXMLImpl.getInstance().getNodeValue(current, "readerUsername")),
                                    format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "creationDate")),
                                    format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "expirationDate")),
                                    BorrowStatus.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "status")),
                                    BookInstanceDaoXMLImpl.getInstance().getBookInstanceByInventoryNumber(
                                            Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "bookInstanceInventoryNumber"))
                                    )
                            )
                    );
                }
            }

            if (results.isEmpty()) {
                throw new NotExistingBorrowingException();
            }
        } catch (ParseException| BookInstanceNotFoundException |BookNotFoundException e) {
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
        Borrowing result = null;

        try {

            for (int i=0; i < borrowings.getLength(); i++) {
                Element current = (Element) borrowings.item(i);
                if (BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID").equalsIgnoreCase(String.valueOf(id))) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    result = new Borrowing(
                            Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID")),
                            BookDaoXMLImpl.getInstance().getReaderByUsername(BookDaoXMLImpl.getInstance().getNodeValue(current, "readerUsername")),
                            format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "creationDate")),
                            format.parse(BookDaoXMLImpl.getInstance().getNodeValue(current, "expirationDate")),
                            BorrowStatus.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "status")),
                            BookInstanceDaoXMLImpl.getInstance().getBookInstanceByInventoryNumber(
                                    Long.valueOf(BookDaoXMLImpl.getInstance().getNodeValue(current, "bookInstanceInventoryNumber"))
                            )
                    );
                }
            }
        } catch (ParseException| BookInstanceNotFoundException |BookNotFoundException|NotExistingReaderException e) {
            throw new NotExistingBorrowingException(e);
        }

        if (result == null)
            throw new NotExistingBorrowingException();

        return result;
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
            if (BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID").equals(String.valueOf(borrowing.getBorrowID()))) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                BookDaoXMLImpl.getInstance().getNode(current, "readerUsername").setTextContent(borrowing.getReader().getUsername());
                BookDaoXMLImpl.getInstance().getNode(current, "creationDate").setTextContent(format.format(borrowing.getCreationDate()));
                BookDaoXMLImpl.getInstance().getNode(current, "expirationDate").setTextContent(format.format(borrowing.getExpirationDate()));
                BookDaoXMLImpl.getInstance().getNode(current, "status").setTextContent(borrowing.getStatus().toString());
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
            if (BookDaoXMLImpl.getInstance().getNodeValue(current, "borrowID").equals(borrowID)) {
                current.getParentNode().removeChild(current);
                return;
            }
        }

        throw new NotExistingBorrowingException();
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
