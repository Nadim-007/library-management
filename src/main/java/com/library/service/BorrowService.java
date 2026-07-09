package com.library.service;

import com.library.entity.BorrowRecord;
import java.util.List;

public interface BorrowService {
    BorrowRecord borrowBook(Long memberId, Long bookId);
    BorrowRecord returnBook(Long borrowRecordId);
    List<BorrowRecord> getAllBorrowRecords();
    List<BorrowRecord> getBorrowsByMember(Long memberId);
    List<BorrowRecord> getOverdueRecords();
}