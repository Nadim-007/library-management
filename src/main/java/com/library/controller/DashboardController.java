package com.library.controller;

import com.library.repository.*;
import com.library.entity.BorrowRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks",   bookRepository.count());
        model.addAttribute("totalMembers", memberRepository.count());
        model.addAttribute("totalBorrows", borrowRecordRepository.count());
        model.addAttribute("overdueCount",
            borrowRecordRepository.findOverdueRecords(LocalDate.now()).size());
        model.addAttribute("recentBorrows",
            borrowRecordRepository.findAll()
                .stream().limit(5).toList());
        return "dashboard";
    }
}
