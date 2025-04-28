package com.example.bookstore.controller;

import com.example.bookstore.dto.UpdateBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
@Tag(name = "Book API", description = "Operations related to books")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Retrieve all books",
            description = "Retrieves a list of books. Supports filtering by minimum and/or maximum price.",
            parameters = {
                    @Parameter(
                            name = "minPrice",
                            in = ParameterIn.QUERY,
                            description = "Minimum price to filter books",
                            schema = @Schema(type = "number", format = "BigDecimal")
                    ),
                    @Parameter(
                            name = "maxPrice",
                            in = ParameterIn.QUERY,
                            description = "Maximum price to filter books",
                            schema = @Schema(type = "number", format = "BigDecimal")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of books",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) BigDecimal minPrice,
                                                  @RequestParam(required = false) BigDecimal maxPrice) {
        List<Book> books;

        if (minPrice != null && maxPrice != null) {
            books = bookService.getBooksByPrice(minPrice, maxPrice);
        } else if (minPrice != null) {
            books = bookService.getBooksByMinPrice(minPrice);
        } else if (maxPrice != null) {
            books = bookService.getBooksByMaxPrice(maxPrice);
        } else {
            books = bookService.getAllBooks();
        }
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Create a new book",
            description = "Creates a new book record in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping("/")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book newBook = bookService.createBook(book);
        return ResponseEntity.ok(newBook);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Retrieves a single book by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true)
            @PathVariable("id") long id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with id " + id + " not found");
        }
        return ResponseEntity.ok(book);
    }

    @Operation(
            summary = "Update an existing book",
            description = "Updates the details of an existing book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID of the book to update", required = true)
            @PathVariable("id") long id,
            @RequestBody UpdateBookDTO book) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes a book record by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true)
            @PathVariable("id") long id) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }
}
