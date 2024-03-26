package com.festago.member.repository;

import com.festago.auth.domain.SocialType;
import com.festago.member.domain.Member;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long id);

    void delete(Member member);

    boolean existsById(Long id);

    long count();

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
