package com.library.controller;

import com.library.entity.Member;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("statuses", Member.MembershipStatus.values());
        return "members/form";
    }

    @PostMapping("/add")
    public String addMember(@Valid @ModelAttribute("member") Member member,
                            BindingResult result, Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Member.MembershipStatus.values());
            return "members/form";
        }
        try {
            memberService.saveMember(member);
            redirectAttributes.addFlashAttribute("successMessage", "Member registered!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", Member.MembershipStatus.values());
            return "members/form";
        }
        return "redirect:/members/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.getMemberById(id));
        model.addAttribute("statuses", Member.MembershipStatus.values());
        return "members/form";
    }

    @PostMapping("/edit/{id}")
    public String updateMember(@PathVariable Long id,
                               @Valid @ModelAttribute("member") Member member,
                               BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Member.MembershipStatus.values());
            return "members/form";
        }
        memberService.updateMember(id, member);
        redirectAttributes.addFlashAttribute("successMessage", "Member updated!");
        return "redirect:/members/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        memberService.deleteMember(id);
        redirectAttributes.addFlashAttribute("successMessage", "Member removed.");
        return "redirect:/members/list";
    }

    @GetMapping("/search")
    public String searchMembers(@RequestParam String keyword, Model model) {
        model.addAttribute("members", memberService.searchMembers(keyword));
        model.addAttribute("keyword", keyword);
        return "members/list";
    }
}
