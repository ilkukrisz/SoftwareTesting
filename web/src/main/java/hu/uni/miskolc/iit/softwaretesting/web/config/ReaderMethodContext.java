package hu.uni.miskolc.iit.softwaretesting.web.config;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import hu.uni.miskolc.iit.softwaretesting.service.impl.ReaderBookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Configuration
public class ReaderMethodContext {

    @Bean
    public ReaderBookService readerBookService() { return new ReaderBookServiceImpl(); }

    @Bean
    public BookDAO bookDAO() throws IOException, SAXException, ParserConfigurationException {
        String dbPath = System.getenv("hu_uni_miskolc_iit_softwaretesting_libraryDatabase");
        return new BookDaoXMLImpl(dbPath, dbPath);
    }
}


