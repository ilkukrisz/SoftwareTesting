package hu.uni.miskolc.iit.softwaretesting.dao.config;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.dao.BookInstanceDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.dao.BorrowingDaoXMLImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Configuration
public class DaoConfig {


    @Bean
    public BookInstanceDaoXMLImpl bookInstanceDaoXML() throws IOException, SAXException, ParserConfigurationException {
        String dbPath = System.getenv("hu_uni_miskolc_iit_softwaretesting_libraryDatabase");
        return new BookInstanceDaoXMLImpl(dbPath, dbPath);
    }

    @Bean
    public BorrowingDaoXMLImpl BorrowingDaoXMLImpl() throws IOException, SAXException, ParserConfigurationException {
        String dbPath = System.getenv("hu_uni_miskolc_iit_softwaretesting_libraryDatabase");
        return new BorrowingDaoXMLImpl(dbPath, dbPath);
    }

    @Bean
    public BookDAO bookDaoXML() throws IOException, SAXException, ParserConfigurationException {
        String dbPath = System.getenv("hu_uni_miskolc_iit_softwaretesting_libraryDatabase");
        return new BookDaoXMLImpl(dbPath, dbPath);
    }



}
