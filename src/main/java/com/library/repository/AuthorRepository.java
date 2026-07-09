package com.library.repository;

import com.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Search authors by last name (case-insensitive)
    List<Author> findByLastNameContainingIgnoreCase(String lastName);

    // Check if an author exists by full name
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
