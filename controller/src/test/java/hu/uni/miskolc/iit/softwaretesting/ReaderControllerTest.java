package hu.uni.miskolc.iit.softwaretesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.uni.miskolc.iit.softwaretesting.converterMethods.ConverterMethods;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.BookType;
import hu.uni.miskolc.iit.softwaretesting.dtoTypes.BorrowingType;
import hu.uni.miskolc.iit.softwaretesting.exceptions.EmptyFieldException;
import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidPublishDateException;
import hu.uni.miskolc.iit.softwaretesting.model.*;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
@ContextConfiguration(classes = {ReaderControllerTestContext.class})
@WebAppConfiguration
public class ReaderControllerTest {



    private MockMvc mockMvc;


    @Autowired
    @Mock
    private ReaderBookService readerBookServiceMock;

    @InjectMocks
    private ReaderController controller;

    private Collection<Book> foundBooks = new ArrayList<>();
    private Reader reader;
    private Borrowing borrowing;

    @Before
    public void setUp() throws InvalidPublishDateException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setHandlerExceptionResolvers(this.getSimpleMappingExceptionResolver())
                .build();

        Book book1 = new Book("William", "Not titled", 123456, 2016, Genre.Fiction);
        Book book2 = new Book("William", "Titled", 654321, 2016, Genre.Fiction);


        reader = new Reader("ilkukrisz", new Password("  "), "Ilku", "Krisztián",
                "email@email.hu", "06-99-1234567");
        Calendar creationDate = Calendar.getInstance();
        Calendar expirationDate = Calendar.getInstance();

        expirationDate.add(Calendar.DATE, 10);
        borrowing = new Borrowing(123456, reader,creationDate.getTime(), expirationDate.getTime(), BorrowStatus.REQUESTED,
                new BookInstance(111111, book1, false));
        foundBooks.add(book1);
        foundBooks.add(book2);
    }


    @Test
    public void testGetAllBooks() throws Exception {
        when(readerBookServiceMock.getAllBooks()).thenReturn(this.foundBooks);

        MvcResult res = mockMvc.perform(get("/reader/allbook"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = res.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBookList((ArrayList<Book>) this.foundBooks);

        assertEquals(expectedJSON, actualJSON);
    }


    @Test
    public void testGetbooksByAuthor() throws Exception {

        when(readerBookServiceMock.getBooksByAuthor("William")).thenReturn(foundBooks);

        MvcResult result = mockMvc.perform(get("/reader/booksbyauthor/?author=William"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = result.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBookList((ArrayList<Book>) foundBooks);

        assertEquals(actualJSON, expectedJSON);
    }

    @Test
    public void testGetbooksByAuthorWithWrongValues() throws Exception {

        when(readerBookServiceMock.getBooksByAuthor("")).thenThrow(EmptyFieldException.class);

        mockMvc.perform(get("/reader/booksbyauthor/?author="))
                .andExpect(status().is(500));

    }

    @Test
    public void testgetBookByCategory() throws Exception {

        when(readerBookServiceMock.getBooksByCategory(Genre.Fiction)).thenReturn(foundBooks);

        MvcResult res = mockMvc.perform(get("/reader/booksbycategory/?category=Fiction"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = res.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBookList((ArrayList<Book>) foundBooks);

        assertEquals(expectedJSON, actualJSON);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetBookByCategoryWithWrongValues() throws Exception {

        when(readerBookServiceMock.getBooksByCategory(Genre.valueOf("asd"))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/reader/booksbycategory/?category=asd"));

    }

    @Test
    public void testgetBookByAvailability() throws Exception {

        when(readerBookServiceMock.getBooksByAvailability()).thenReturn(foundBooks);

        MvcResult res = mockMvc.perform(get("/reader/booksbyavailability"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = res.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBookList((ArrayList<Book>) foundBooks);

        assertEquals(expectedJSON, actualJSON);

    }


    @Test
    public void testgetBookByAvailabilableBooksByTitle() throws Exception {

        when(readerBookServiceMock.getAvailableBooksByTitle("title")).thenReturn(foundBooks);

        MvcResult res = mockMvc.perform(get("/reader/aebooksbytitle/?title=title"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = res.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBookList((ArrayList<Book>) foundBooks);

        assertEquals(expectedJSON, actualJSON);

    }

    @Test
    public void testgetBookByAvailabilableBooksByTitleWithEmptyField() throws Exception {

        when(readerBookServiceMock.getAvailableBooksByTitle("")).thenThrow(EmptyFieldException.class);

        mockMvc.perform(get("/reader/aebooksbytitle/?title="))
                .andExpect(status().is(500));

    }

    @Test
    public void testgetBookByYear() throws Exception {

        when(readerBookServiceMock.getBooksByYear(2016)).thenReturn(foundBooks);

        MvcResult res = mockMvc.perform(get("/reader/booksbyyear/?year=2016"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = res.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBookList((ArrayList<Book>) foundBooks);

        assertEquals(expectedJSON, actualJSON);

    }


    @Test
    public void testgetBookByYearWithWrongValues() throws Exception {

        when(readerBookServiceMock.getBooksByYear(100000)).thenThrow(InvalidPublishDateException.class);

        MvcResult res = mockMvc.perform(get("/reader/booksbyyear/?year=100000"))
                .andExpect(status().is(500))
                .andReturn();
    }

    @Test
    public void testgetBookByWithEmptyField() throws Exception {

        mockMvc.perform(get("/reader/booksbyyear/?year="))
                .andExpect(status().is(500))
                .andReturn();

    }

    @Test
    public void testRequestBook() throws Exception {

        mockMvc.perform(post("/reader/requestbook/?isbn=123456&username=ilkukrisz"))
                .andExpect(status().is(500));

    }

    @Test
    public void testRequestBookWithEmptyField() throws Exception {

        MvcResult res = mockMvc.perform(post("/reader/requestbook/?isbn=&username="))
                .andExpect(status().is(500))
                .andReturn();
    }
    
    @Test
    public void testgetBorrowings() throws Exception {

        ArrayList<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(this.borrowing);

        when(readerBookServiceMock.showBorrowings(any(Reader.class))).thenReturn(borrowings);

        MvcResult res = mockMvc.perform(get("/reader/borrowings/?username=bela"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJSON = res.getResponse().getContentAsString();
        String expectedJSON = this.getJsonFromBorrowingList(borrowings);

        assertEquals(expectedJSON, actualJSON);

    }


    @Test
    public void testgetBorrowingsWithEmptyValues() throws Exception {

        ArrayList<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(this.borrowing);

        when(readerBookServiceMock.showBorrowings(any(Reader.class))).thenReturn(borrowings);

        mockMvc.perform(get("/reader/borrowings/?username="))
                .andExpect(status().is(500));

    }




    private String getJsonFromBorrowingList (ArrayList<Borrowing> borrowingList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<BorrowingType> borrowingTypeList = ConverterMethods.convertBorrowingToBorrowingType(borrowingList);
        return mapper.writeValueAsString(borrowingTypeList);
    }


    private String getJsonFromBookList (ArrayList<Book> bookList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<BookType> borrowingTypeList = ConverterMethods.convertBookToBookType(bookList);
        return mapper.writeValueAsString(borrowingTypeList);
    }


    private SimpleMappingExceptionResolver getSimpleMappingExceptionResolver () {
        SimpleMappingExceptionResolver result = new SimpleMappingExceptionResolver();
        result.setDefaultErrorView("Errors/Default");
        result.setDefaultStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return result;
    }
}
