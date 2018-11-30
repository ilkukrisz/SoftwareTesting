package hu.uni.miskolc.iit.softwaretesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.uni.miskolc.iit.softwaretesting.converterMethods.ConverterMethods;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.BorrowingType;
import hu.uni.miskolc.iit.softwaretesting.exceptions.*;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {LibrarianControllerTestContext.class})
@WebAppConfiguration
public class LibrarianControllerTest {

    private MockMvc mockMVC;

    @Autowired
    @Mock
    private LibrarianBookService librarianServiceMock;

    @InjectMocks
    private LibrarianController controller;

    @Mock
    FormattingConversionService conversionService;

    private Borrowing exampleBorrowing1;
    private Borrowing exampleBorrowing2;

    @Before
    public void setUp () throws InvalidPublishDateException {
        conversionService = new DefaultFormattingConversionService();

        MockitoAnnotations.initMocks(this);
        this.mockMVC = MockMvcBuilders
                .standaloneSetup(controller)
                .setHandlerExceptionResolvers(this.getSimpleMappingExceptionResolver())
                .setConversionService(conversionService)
                .build();

        Book book = new Book("Krisz", "Title", (long)97236589, 2005, Genre.Crimi);
        Reader reader = new Reader("ilkukrisz", new Password("  "), "Ilku","Krisztian",
                "apple@apple.com", "06123456789");
        BookInstance bookInstance = new BookInstance((long) 2135510, book, false);

        Calendar calCreationDate = Calendar.getInstance();
        Calendar calExpirationDate = Calendar.getInstance();
        calCreationDate.set(2017, Calendar.OCTOBER, 10);
        calExpirationDate.set(2017, Calendar.NOVEMBER, 9);

        this.exampleBorrowing1 = new Borrowing((long) 98236, reader, calCreationDate.getTime(),
                calExpirationDate.getTime(), BorrowStatus.REQUESTED, bookInstance);
        this.exampleBorrowing2 = new Borrowing(123456, reader, calCreationDate.getTime(),
                calExpirationDate.getTime(), BorrowStatus.BORROWED, bookInstance);
    }

    @Test
    public void testAddNewBook () throws Exception {
        //prevent addBook to write database
        doNothing().when(librarianServiceMock).addBook(Matchers.any(Book.class));

        //perform POST request
        mockMVC.perform(this.getBookFormRequest("/librarian/addbook"))
                //check HTTP response status code
                .andExpect(status().isOk());
    }

    @Test
    public void testAddExistingBook () throws Exception {
        doThrow(new AlreadyExistingBookException()).when(librarianServiceMock).addBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/addbook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddBookWithWrongIsbn () throws Exception {
        doThrow(new WrongISBNException()).when(librarianServiceMock).addBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/addbook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddBookPersistenceException () throws Exception {
        doThrow(new PersistenceException()).when(librarianServiceMock).addBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/addbook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddBookNotExistingGenreException () throws Exception {
        doThrow(new NotExistingGenreException()).when(librarianServiceMock).addBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/addbook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateBook () throws Exception {
        doNothing().when(librarianServiceMock).updateBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/updatebook"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateBookWithNotExistingGenre () throws Exception {
        doThrow(new NotExistingGenreException()).when(librarianServiceMock).updateBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/updatebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateBookPersistenceException () throws Exception {
        doThrow(new PersistenceException()).when(librarianServiceMock).updateBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/updatebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateBookWithWrongIsbn () throws Exception {
        doThrow(new WrongISBNException()).when(librarianServiceMock).updateBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/updatebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateNotExistingBook () throws Exception {
        doThrow(new BookNotFoundException()).when(librarianServiceMock).updateBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/updatebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteBook () throws Exception {
        doNothing().when(librarianServiceMock).deleteBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/deletebook"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBookPersistenceError () throws Exception {
        doThrow(new PersistenceException()).when(librarianServiceMock).deleteBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/deletebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteNotExistingBook () throws Exception {
        doThrow(new BookNotFoundException()).when(librarianServiceMock).deleteBook(Matchers.any(Book.class));

        mockMVC.perform(this.getBookFormRequest("/librarian/deletebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCountBooks () throws Exception {
        int expectedCount = 16;
        when(librarianServiceMock.countBooks()).thenReturn(expectedCount);

        MvcResult res = mockMVC.perform(get("/librarian/countbooks")).andReturn();
        int actualCount = Integer.valueOf(res.getResponse().getContentAsString());

        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testAddBookInstance () throws Exception {
        doNothing().when(librarianServiceMock).addBookInstances(Matchers.any(BookInstance.class));

        mockMVC.perform(this.getBookInstanceFormRequest("/librarian/addbi"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddAlreadyExistingBookInstance () throws Exception {
        doThrow(new AlreadyExistingBookInstance()).when(librarianServiceMock).addBookInstances(Matchers.any(BookInstance.class));

        mockMVC.perform(this.getBookInstanceFormRequest("/librarian/addbi"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAssBookInstancePersistenceError () throws Exception {
        doThrow(new PersistenceException()).when(librarianServiceMock).addBookInstances(Matchers.any(BookInstance.class));

        mockMVC.perform(this.getBookInstanceFormRequest("/librarian/addbi"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteBookInstance () throws Exception {
        doNothing().when(librarianServiceMock).deleteBookInstances(Matchers.any(BookInstance.class));

        mockMVC.perform(this.getBookInstanceFormRequest("/librarian/deletebi"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNotExistingBookInstance () throws Exception {
        doThrow(new BookInstanceNotFound()).when(librarianServiceMock).deleteBookInstances(Matchers.any(BookInstance.class));

        mockMVC.perform(this.getBookInstanceFormRequest("/librarian/deletebi"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testLendBook () throws Exception {
        doNothing().when(librarianServiceMock).lendBook(Matchers.any(Borrowing.class));

        mockMVC.perform(this.getBorrowingFormRequest("/librarian/lendbook"))
            .andExpect(status().isOk());
    }

    @Test
    public void testLendBookPersistenceError () throws Exception {
        doThrow(new PersistenceException()).when(librarianServiceMock).lendBook(Matchers.any(Borrowing.class));

        mockMVC.perform(this.getBorrowingFormRequest("/librarian/lendbook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testLendNotExistingBorrowing () throws Exception {
        doThrow(new NotExistingBorrowingException()).when(librarianServiceMock).lendBook(Matchers.any(Borrowing.class));

        mockMVC.perform(this.getBorrowingFormRequest("/librarian/lendbook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testListBorrowings () throws Exception {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(exampleBorrowing1);
        borrowings.add(exampleBorrowing2);

        when(librarianServiceMock.listBorrowings()).thenReturn(borrowings);

        MvcResult res = mockMVC.perform(get("/librarian/listborrowings"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = res.getResponse().getContentAsString();
        String expectedJson = this.getJsonFromBorrowingList(borrowings);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testListRequests () throws Exception {
        ArrayList<Borrowing> requests = new ArrayList<>();
        this.exampleBorrowing1.setStatus(BorrowStatus.REQUESTED);
        this.exampleBorrowing2.setStatus(BorrowStatus.REQUESTED);
        requests.add(this.exampleBorrowing1);
        requests.add(this.exampleBorrowing2);

        when(librarianServiceMock.listRequests()).thenReturn(requests);

        MvcResult res = mockMVC.perform(get("/librarian/listrequests"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = res.getResponse().getContentAsString();
        String expectedJson = this.getJsonFromBorrowingList(requests);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testListRequestsWhenNoRequestExists () throws Exception {
        doThrow(new NotExistingBorrowingException()).when(librarianServiceMock).listRequests();

        mockMVC.perform(get("/librarian/listrequests"))
                .andExpect(status().isInternalServerError());
    }

    private MockHttpServletRequestBuilder getBorrowingFormRequest (String urlTemplate) {
        return this.getFormPostRequest(urlTemplate)
                .param("borrowID", "159741852963")
                .param("borrowStatus", "BORROWED")
                .param("bookInstance.inventoryNumber", "123405678099")
                .param("bookInstance.book.author", "teszt")
                .param("bookInstance.book.title", "tesztteszt")
                .param("bookInstance.book.publishDate", "2010")
                .param("bookInstance.book.genre", "Scifi")
                .param("reader.username", "bela")
                .param("reader.password.hashedPassword",
                        "$2a$04$UG7M8/jwMYVAkqyyLTT4OOsUN2JZtuHoCSEpYYTYYBAp9ESsKZkmy")
                .param("reader.firstName", "asdasdsad")
                .param("reader.lastName", "asdasdasdas")
                .param("reader.email", "sda@sdasd.com")
                .param("reader.mobileNumber", "06301234567")
                .param("reader.password.saltStrength", "7");
    }

    private MockHttpServletRequestBuilder getBookInstanceFormRequest (String urlTemplate) {
        return this.getFormPostRequest(urlTemplate)
                .param("book.author", "Author Name")
                .param("book.title", "Book Title")
                .param("book.isbn", "159741")
                .param("book.publishDate", "2010")
                .param("book.genre", "Scifi")
                .param("inventoryNumber", "159753")
                .param("isLoaned", "false");
    }

    private MockHttpServletRequestBuilder getBookFormRequest (String urlTemplate) {
        return this.getFormPostRequest(urlTemplate)
                .param("author", "Author Name")
                .param("title", "Book Title")
                .param("isbn", "159741")
                .param("publishDate", "2010")
                .param("genre", "Scifi");
    }

    private MockHttpServletRequestBuilder getFormPostRequest (String urlTemplate) {
        return post(urlTemplate).contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    private String getJsonFromBorrowingList (ArrayList<Borrowing> borrowingList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<BorrowingType> borrowingTypeList = ConverterMethods.convertBorrowingToBorrowingType(borrowingList);
        return mapper.writeValueAsString(borrowingTypeList);
    }

    private SimpleMappingExceptionResolver getSimpleMappingExceptionResolver () {
        SimpleMappingExceptionResolver result = new SimpleMappingExceptionResolver();
        result.setDefaultErrorView("Errors/Default");
        result.setDefaultStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return result;
    }
}
