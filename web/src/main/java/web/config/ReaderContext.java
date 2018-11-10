package web.config;

import hu.uni.miskolc.iit.softwaretesting.ReaderController;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@Configuration
@ComponentScan
public class ReaderContext {

    private final ReaderBookService readerBookService;

    @Autowired
    public ReaderContext(ReaderBookService readerBookService) { this.readerBookService = readerBookService; }

    @Bean
    public ReaderController readerController() {
        return new ReaderController(readerBookService);
    }

}
