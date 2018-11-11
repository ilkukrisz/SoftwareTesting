package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;

import hu.uni.miskolc.iit.softwaretesting.service.impl.CommandLine;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class BookDAOTest {

    private BookDaoXMLImpl dao;

    public BookDAOTest () throws IOException, SAXException, ParserConfigurationException {
        String inputFile = CommandLine.findMyDatabaseFile();
        this.dao = new BookDaoXMLImpl(inputFile, inputFile);
    }

    //TODO
}
