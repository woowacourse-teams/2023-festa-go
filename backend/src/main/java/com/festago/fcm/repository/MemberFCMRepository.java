package com.festago.fcm.repository;

import com.festago.fcm.domain.MemberFCM;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFCMRepository extends JpaRepository<MemberFCM, Long> {

    List<MemberFCM> findByMemberId(Long memberId);
}
