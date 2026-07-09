package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search books by title (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find all books by a specific author
    List<Book> findByAuthorId(Long authorId);

    // Find books by genre
    List<Book> findByGenreIgnoreCase(String genre);

    // Find only books that are currently available
    List<Book> findByAvailableCopiesGreaterThan(int count);

    // Custom JPQL — search by title OR author name
    @Query("SELECT b FROM Book b JOIN b.author a " +
           "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByKeyword(@Param("keyword") String keyword);
}
