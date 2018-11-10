package hu.uni.miskolc.iit.softwaretesting.web.config;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import hu.uni.miskolc.iit.softwaretesting.service.impl.ReaderBookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ReaderMethodContext {

    @Bean
    public ReaderBookService readerBookService() { return new ReaderBookServiceImpl(); }


    @Bean
    public BookDAO dao() { return new BookDaoXMLImpl(new File("../../../../dao/resources/database.xml"), new File("../../../../dao/resources/database.xml")); }


}


