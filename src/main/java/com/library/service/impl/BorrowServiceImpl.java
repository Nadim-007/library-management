package com.library.service.impl;

import com.library.entity.*;
import com.library.entity.BorrowRecord.BorrowStatus;
import com.library.repository.*;
import com.library.service.BorrowService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public BorrowRecord borrowBook(Long memberId, Long bookId) {

        // 1. Fetch member & book
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        // 2. Business rules
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for: " + book.getTitle());
        }
        if (member.getMembershipStatus() != Member.MembershipStatus.ACTIVE) {
            throw new IllegalStateException("Member account is not active");
        }
        if (borrowRecordRepository.existsByMemberIdAndBookIdAndStatus(
                memberId, bookId, BorrowStatus.BORROWED)) {
            throw new IllegalStateException("Member already has this book borrowed");
        }

        // 3. Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // 4. Create borrow record
        BorrowRecord record = BorrowRecord.builder()
            .member(member)
            .book(book)
            .borrowDate(LocalDate.now())
            .dueDate(LocalDate.now().plusDays(14))  // 2-week loan
            .status(BorrowStatus.BORROWED)
            .build();

        return borrowRecordRepository.save(record);
    }

    @Override
    @Transactional
    public BorrowRecord returnBook(Long borrowRecordId) {

        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
            .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("Book already returned");
        }

        // Increase available copies
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Update record
        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);

        return borrowRecordRepository.save(record);
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    @Override
    public List<BorrowRecord> getBorrowsByMember(Long memberId) {
        return borrowRecordRepository.findByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
    }

    @Override
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordRepository.findOverdueRecords(LocalDate.now());
    }
}