package com.festago.fcm.repository;

import com.festago.fcm.domain.MemberFCM;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberFCMRepository extends JpaRepository<MemberFCM, Long> {
    
    @Query("SELECT mf.fcmToken FROM MemberFCM mf WHERE mf.memberId = :memberId")
    List<String> findAllTokenByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT mf.fcmToken FROM MemberFCM mf WHERE mf.memberId IN :memberIds")
    List<String> findAllTokenByMemberIdIn(@Param("memberIds") List<Long> memberIds);
}
