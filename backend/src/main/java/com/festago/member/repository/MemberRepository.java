package com.festago.member.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.domain.SocialType;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    default Member getOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    Member save(Member member);

    Optional<Member> findById(Long id);

    void delete(Member member);

    boolean existsById(Long id);

    long count();

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
