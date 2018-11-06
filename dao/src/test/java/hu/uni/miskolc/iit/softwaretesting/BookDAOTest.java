package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAOImpl;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class BookDAOTest {

    private BookDAOImpl dao;

    public BookDAOTest () throws IOException, SAXException, ParserConfigurationException {
        File inputFile = new File("resources/database.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

        this.dao = new BookDAOImpl(docBuilder.parse(inputFile), inputFile);
    }

    //TODO
}
