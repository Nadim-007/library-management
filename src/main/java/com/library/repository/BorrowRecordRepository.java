package com.library.repository;

import com.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // All active borrows for a specific member
    List<BorrowRecord> findByMemberIdAndStatus(Long memberId, BorrowRecord.BorrowStatus status);

    // All active borrows for a specific book
    List<BorrowRecord> findByBookIdAndStatus(Long bookId, BorrowRecord.BorrowStatus status);

    // Find overdue records (due date passed, not returned)
    @Query("SELECT br FROM BorrowRecord br WHERE br.status = 'BORROWED' " +
           "AND br.dueDate < :today")
    List<BorrowRecord> findOverdueRecords(@Param("today") LocalDate today);

    // Count how many times a book has been borrowed
    long countByBookId(Long bookId);

    // Check if a member currently has a specific book
    boolean existsByMemberIdAndBookIdAndStatus(
        Long memberId, Long bookId, BorrowRecord.BorrowStatus status
    );
}
