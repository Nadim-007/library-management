package com.library.service;

import com.library.entity.Member;
import java.util.List;

public interface MemberService {
    List<Member> getAllMembers();
    Member getMemberById(Long id);
    Member saveMember(Member member);
    Member updateMember(Long id, Member member);
    void deleteMember(Long id);
    List<Member> searchMembers(String name);
}
