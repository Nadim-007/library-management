package com.library.service.impl;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import com.library.service.MemberService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
    }

    @Override
    @Transactional
    public Member saveMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + member.getEmail());
        }
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateMember(Long id, Member updatedMember) {
        Member existing = getMemberById(id);
        existing.setFullName(updatedMember.getFullName());
        existing.setPhone(updatedMember.getPhone());
        existing.setMembershipStatus(updatedMember.getMembershipStatus());
        return memberRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }

    @Override
    public List<Member> searchMembers(String name) {
        return memberRepository.findByFullNameContainingIgnoreCase(name);
    }
}