package com.festago.member.repository;

import com.festago.auth.domain.SocialType;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getOrThrow(Long memberId) {
        return findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
