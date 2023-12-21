package com.festago.fcm.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.member.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberFCMServiceTest {

    @InjectMocks
    MemberFCMService memberFCMService;

    @Mock
    MemberFCMRepository memberFCMRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    void 유저의_FCM_정보를_가져온다() {
        // given
        List<MemberFCM> memberFCMs = List.of(
            new MemberFCM(1L, 1L, "token"),
            new MemberFCM(2L, 1L, "token2")
        );
        given(memberFCMRepository.findAllByMemberId(anyLong()))
            .willReturn(memberFCMs);

        List<String> expect = memberFCMs.stream()
            .map(MemberFCM::getFcmToken)
            .toList();

        // when
        List<String> actual = memberFCMService.findAllMemberFCMTokens(1L);

        // then
        assertThat(actual).containsAll(expect);
    }

    @Test
    void 유저의_새로운_FCM_토큰이라면_저장() {
        // given
        String fcmToken = "fcmToken";
        Long memberId = 1L;
        given(memberRepository.existsById(anyLong()))
            .willReturn(true);
        given(memberFCMRepository.existsByMemberIdAndFcmToken(anyLong(), anyString()))
            .willReturn(false);

        // when
        memberFCMService.saveMemberFCM(memberId, fcmToken);

        // then
        verify(memberFCMRepository, times(1))
            .save(any(MemberFCM.class));
    }

    @Test
    void 유저의_FCM_토큰이_존재하면_저장하지_않는다() {
        // given
        String fcmToken = "fcmToken";
        Long memberId = 1L;
        given(memberRepository.existsById(anyLong()))
            .willReturn(true);
        given(memberFCMRepository.existsByMemberIdAndFcmToken(anyLong(), anyString()))
            .willReturn(true);

        // when
        memberFCMService.saveMemberFCM(memberId, fcmToken);

        // then
        verify(memberFCMRepository, never())
            .save(any(MemberFCM.class));
    }

    @Test
    void 유저의_FCM_삭제() {
        // given
        Long memberId = 1L;

        // when
        memberFCMService.deleteAllMemberFCM(memberId);

        // then
        verify(memberFCMRepository, times(1))
            .deleteAllByMemberId(memberId);
    }
}
