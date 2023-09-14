package com.festago.member.repository;

import com.festago.auth.domain.SocialType;
import com.festago.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
