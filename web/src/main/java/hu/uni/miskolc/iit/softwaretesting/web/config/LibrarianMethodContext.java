package hu.uni.miskolc.iit.softwaretesting.web.config;

import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import hu.uni.miskolc.iit.softwaretesting.service.impl.LibrarianBookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
public class LibrarianMethodContext {

    @Bean
    @DependsOn("bookDAO")
    public LibrarianBookService librarianBookService() { return new LibrarianBookServiceImpl(); }

}
