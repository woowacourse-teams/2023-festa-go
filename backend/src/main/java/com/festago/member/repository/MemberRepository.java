package com.festago.member.repository;

import com.festago.auth.domain.SocialType;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    default Member getOrThrow(Long memberId) {
        return findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    void delete(Member member);

    long count();
}
