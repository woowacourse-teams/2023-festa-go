package com.festago.fcm.repository;

import com.festago.fcm.domain.MemberFCM;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberFCMRepository extends Repository<MemberFCM, Long> {

    MemberFCM save(MemberFCM memberFCM);

    List<MemberFCM> findByMemberId(Long memberId);

    Optional<MemberFCM> findByMemberIdAndFcmToken(Long memberId, String fcmToken);

    void deleteAllByMemberId(Long memberId);
}
