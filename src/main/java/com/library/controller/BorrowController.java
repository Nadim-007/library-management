package com.library.controller;

import com.library.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final BookService bookService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String listBorrows(Model model) {
        model.addAttribute("borrows", borrowService.getAllBorrowRecords());
        return "borrow/list";
    }

    @GetMapping("/new")
    public String showBorrowForm(Model model) {
        model.addAttribute("books",   bookService.getAvailableBooks());
        model.addAttribute("members", memberService.getAllMembers());
        return "borrow/form";
    }

    @PostMapping("/new")
    public String borrowBook(@RequestParam Long memberId,
                             @RequestParam Long bookId,
                             RedirectAttributes redirectAttributes) {
        try {
            borrowService.borrowBook(memberId, bookId);
            redirectAttributes.addFlashAttribute("successMessage", "Book borrowed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/borrow/list";
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            borrowService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book returned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/borrow/list";
    }

    @GetMapping("/overdue")
    public String overdueList(Model model) {
        model.addAttribute("borrows", borrowService.getOverdueRecords());
        return "borrow/list";
    }

    @GetMapping("/member/{id}")
    public String memberBorrows(@PathVariable Long id, Model model) {
        model.addAttribute("borrows", borrowService.getBorrowsByMember(id));
        model.addAttribute("member", memberService.getMemberById(id));
        return "borrow/list";
    }
}
