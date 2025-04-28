package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebMvcTest(value = BookController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

//    @Autowired
//    MockMvc mvc;
//
//    @MockBean
//    BookService bookService;
//
//    private List<Book> getBookList() {
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setDescription("Book Description");
//        book.setIsbn("123456789");
//        book.setGenre("Genre");
//        book.setPrice(BigDecimal.valueOf(49874));
//        return List.of(book, new Book());
//    }
//
//
//    @Test
//    void testFindAll() throws Exception {
//        Mockito.when(this.bookService.getAllBooks()).thenReturn(getBookList());
//
//        mvc.perform(get("http://localhost:8080/book/")).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    void testFindById() throws Exception {
//        Mockito.when(this.bookService.getBookById(1)).thenReturn(Optional.of(new Book()));
//
//        mvc.perform(get("http://localhost:8080/book/1")).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    void testSave() throws Exception {
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setDescription("Book Description");
//        book.setIsbn("123456789");
//        book.setGenre("Genre");
//        book.setPrice(BigDecimal.valueOf(49874));
//        book.setPublicationDate(LocalDate.now());
//
//        Mockito.when(this.bookService.createBook(Mockito.any(Book.class))).thenReturn(book);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String bookJson = objectMapper.writeValueAsString(book);
//
//        mvc.perform(post("http://localhost:8080/book/").contentType(MediaType.APPLICATION_JSON).content(bookJson)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book Title")).andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Book Description")).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("123456789")).andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Genre")).andExpect(MockMvcResultMatchers.jsonPath("$.price").value(49874));
//    }


}
