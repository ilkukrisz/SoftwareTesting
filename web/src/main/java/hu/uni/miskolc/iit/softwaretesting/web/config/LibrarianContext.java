package hu.uni.miskolc.iit.softwaretesting.web.config;

import hu.uni.miskolc.iit.softwaretesting.LibrarianController;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan
public class LibrarianContext {

   /* private final LibrarianBookService librarianBookService;

    @Autowired
    public LibrarianContext(LibrarianBookService librarianBookService) { this.librarianBookService = librarianBookService; }

    @Bean
    public LibrarianController librariancontroller(LibrarianBookService librarianBookService){ return new LibrarianController(librarianBookService); }*/


}
