package com.library.service.impl;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
import com.library.service.AuthorService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
    }

    @Override
    @Transactional
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author updateAuthor(Long id, Author updatedAuthor) {
        Author existing = getAuthorById(id);
        existing.setFirstName(updatedAuthor.getFirstName());
        existing.setLastName(updatedAuthor.getLastName());
        existing.setNationality(updatedAuthor.getNationality());
        return authorRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }

    @Override
    public List<Author> searchByLastName(String lastName) {
        return authorRepository.findByLastNameContainingIgnoreCase(lastName);
    }
}
