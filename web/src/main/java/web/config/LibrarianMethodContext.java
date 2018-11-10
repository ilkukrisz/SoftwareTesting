package web.config;

import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import hu.uni.miskolc.iit.softwaretesting.service.impl.LibrarianBookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LibrarianMethodContext {

    @Bean
    public LibrarianBookService librarianBookService() { return new LibrarianBookServiceImpl(); }

}
