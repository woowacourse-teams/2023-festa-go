package com.festago.fcm.repository;

import com.festago.fcm.domain.MemberFCM;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MemberFCMRepository extends Repository<MemberFCM, Long> {

    MemberFCM save(MemberFCM memberFCM);

    List<MemberFCM> findAllByMemberId(Long memberId);

    @Modifying
    @Query("delete from MemberFCM mf where mf.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from MemberFCM mf where mf in :memberFCMs")
    void deleteByIn(List<MemberFCM> memberFCMs);
}
