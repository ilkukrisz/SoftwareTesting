package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private LibrarianBookService librarianBookService;

    @Autowired
    public LibrarianController(LibrarianBookService librarianBookService) { this.librarianBookService = librarianBookService; }


}
