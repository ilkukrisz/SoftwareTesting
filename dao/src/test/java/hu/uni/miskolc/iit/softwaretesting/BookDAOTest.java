package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class BookDAOTest {

    private BookDaoXMLImpl dao;

    public BookDAOTest () throws IOException, SAXException, ParserConfigurationException {
        String inputFile = System.getenv("hu_uni_miskolc_iit_softwaretesting_libraryDatabase");
        this.dao = new BookDaoXMLImpl(inputFile, inputFile);
    }

}
