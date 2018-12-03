package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import hu.uni.miskolc.iit.softwaretesting.service.impl.ReaderBookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ReaderControllerTestContext {
    @Bean
    ReaderBookService readerBookService() {
        return new ReaderBookServiceImpl();
    }

    @Bean
    BookDaoXMLImpl bookDAO () throws IOException, SAXException, ParserConfigurationException {
        return new BookDaoXMLImpl("../dao/resources/database.xml", "../dao/resources/database.xml");
    }
}
