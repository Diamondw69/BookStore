package com.example.bookstore.controller;


import com.example.bookstore.dto.UpdateAuthorDTO;
import com.example.bookstore.entity.Author;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/author")
public class AuthorController {

    private final AuthorService userRepository;

    @GetMapping("/")
    public ResponseEntity<List<Author>> get(@RequestParam(required = false) String lastName, @RequestParam(required = false) String firstName) {
        if (lastName != null) {
            List<Author> authors = userRepository.findByLastName(lastName);
            if (authors.isEmpty()) {
                throw new ResourceNotFoundException("No authors found");
            }
            return ResponseEntity.ok(authors);
        }
        if (firstName != null) {
            List<Author> authors = userRepository.findByFirstName(firstName);
            if (authors.isEmpty()) {
                throw new ResourceNotFoundException("No authors found");
            }
            return ResponseEntity.ok(authors);
        }
        List<Author> authors = userRepository.getAll();
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No authors found");
        }
        return ResponseEntity.ok(authors);
    }

    @PostMapping("/")
    ResponseEntity<Author> create(@Valid @RequestBody Author author){
        Author newAuthor = userRepository.create(author);
        return ResponseEntity.ok(newAuthor);
    }

    @GetMapping("/{id}")
    ResponseEntity<Author> getById(@PathVariable long id){
        Author author = userRepository.getById(id);
        if (author == null) {
            throw new ResourceNotFoundException("Author with id " + id + " is not found");
        }
        return ResponseEntity.ok(author);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Author> update(@PathVariable long id, @RequestBody UpdateAuthorDTO author){
        Author updateAuthor = userRepository.getById(id);
        if (updateAuthor == null) {
            throw new ResourceNotFoundException("Author with id " + id + " is not found");
        }
        Author updatedAuthor=userRepository.update(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Author> delete(@PathVariable long id){
        userRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
