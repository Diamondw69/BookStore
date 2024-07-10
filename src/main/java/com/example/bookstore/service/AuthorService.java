package com.example.bookstore.service;

import com.example.bookstore.dto.UpdateAuthorDTO;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author getById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackOn= {SQLException.class})
    public Author create(Author author) {
        for (Book book : author.getBooks()) {
            book.setAuthor(author);
        }
        return authorRepository.save(author);
    }

    @Transactional(rollbackOn= {SQLException.class})
    public Author update(Long id, UpdateAuthorDTO updateAuthorDTO) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            Author updateAuthor=author.get();
            if (updateAuthorDTO.getFirstName() != null){
                updateAuthor.setFirstName(updateAuthorDTO.getFirstName());
            }
            if (updateAuthorDTO.getLastName() != null){
                updateAuthor.setLastName(updateAuthorDTO.getLastName());
            }
            if (updateAuthorDTO.getBirthDate() != null){
                updateAuthor.setBirthDate(updateAuthorDTO.getBirthDate());
            }
            if (updateAuthorDTO.getBiography()  != null){
                updateAuthor.setBiography(updateAuthorDTO.getBiography());
            }
            if (!isEmpty(updateAuthorDTO.getBooks())){
                for (Book book : updateAuthorDTO.getBooks()) {
                    updateAuthor.addBook(book);
                }
            }
            return authorRepository.save(updateAuthor);
        }else{
            return null;
        }
    }

    @Transactional(rollbackOn=  {SQLException.class})
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Author> findByLastName(String lastName)  {
        return authorRepository.findAuthorsByLastName(lastName);
    }

    public List<Author> findByFirstName(String lastName)  {
        return authorRepository.findAuthorsByFirstName(lastName);
    }
}
