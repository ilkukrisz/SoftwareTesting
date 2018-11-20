package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import hu.uni.miskolc.iit.softwaretesting.service.impl.LibrarianBookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class LibrarianControllerTestContext {
    @Bean
    LibrarianBookService librarianBookService () {
        return new LibrarianBookServiceImpl();
    }

    @Bean
    BookDaoXMLImpl bookDAO () throws IOException, SAXException, ParserConfigurationException {
        return new BookDaoXMLImpl("../dao/resources/database.xml", "../dao/resources/database.xml");
    }
}
