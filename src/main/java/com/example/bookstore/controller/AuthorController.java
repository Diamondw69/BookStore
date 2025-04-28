package com.example.bookstore.controller;

import com.example.bookstore.dto.UpdateAuthorDTO;
import com.example.bookstore.entity.Author;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.AuthorService;
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

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/author")
@Tag(name = "Author API", description = "Operations related to authors")
public class AuthorController {

    private final AuthorService userRepository;

    @Operation(
            summary = "Retrieve authors",
            description = "Retrieves a list of authors. You can filter by last name and/or first name.",
            parameters = {
                    @Parameter(
                            name = "lastName",
                            in = ParameterIn.QUERY,
                            description = "Filter authors by last name",
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "firstName",
                            in = ParameterIn.QUERY,
                            description = "Filter authors by first name",
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authors retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
                    @ApiResponse(responseCode = "404", description = "No authors found")
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<Author>> get(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName) {
        List<Author> authors;

        if (lastName != null && firstName != null) {
            authors = userRepository.findByLastNameAndFirstName(lastName, firstName);
        } else if (lastName != null) {
            authors = userRepository.findByLastName(lastName);
        } else if (firstName != null) {
            authors = userRepository.findByFirstName(firstName);
        } else {
            authors = userRepository.getAll();
        }

        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No authors found");
        }

        return ResponseEntity.ok(authors);
    }

    @Operation(
            summary = "Create a new author",
            description = "Creates a new author in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Author created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping("/")
    public ResponseEntity<Author> create(
            @Valid @RequestBody Author author) {
        Author newAuthor = userRepository.create(author);
        return ResponseEntity.ok(newAuthor);
    }

    @Operation(
            summary = "Get author by ID",
            description = "Retrieves an author by their unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Author found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(
            @Parameter(description = "ID of the author to retrieve", required = true)
            @PathVariable long id) {
        Author author = userRepository.getById(id);
        if (author == null) {
            throw new ResourceNotFoundException("Author with id " + id + " is not found");
        }
        return ResponseEntity.ok(author);
    }

    @Operation(
            summary = "Update an author",
            description = "Updates an existing author identified by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Author updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Author> update(
            @Parameter(description = "ID of the author to update", required = true)
            @PathVariable long id,
            @RequestBody UpdateAuthorDTO author) {
        Author updateAuthor = userRepository.getById(id);
        if (updateAuthor == null) {
            throw new ResourceNotFoundException("Author with id " + id + " is not found");
        }
        Author updatedAuthor = userRepository.update(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @Operation(
            summary = "Delete an author",
            description = "Deletes an author by their unique ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Author> delete(
            @Parameter(description = "ID of the author to delete", required = true)
            @PathVariable long id) {
        userRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
