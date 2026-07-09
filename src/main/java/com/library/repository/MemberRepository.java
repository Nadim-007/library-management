package com.library.repository;

import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Find member by email (for login / uniqueness check)
    Optional<Member> findByEmail(String email);

    // Search members by name
    List<Member> findByFullNameContainingIgnoreCase(String name);

    // Find all active members
    List<Member> findByMembershipStatus(Member.MembershipStatus status);

    // Check if email already registered
    boolean existsByEmail(String email);
}
