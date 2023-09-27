package com.festago.fcm.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.MemberFCMResponse;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberFCMServiceTest {

    @Mock
    MemberFCMRepository memberFCMRepository;

    @Mock
    AuthExtractor authExtractor;

    @InjectMocks
    MemberFCMService memberFCMService;

    @Test
    void 유저의_FCM_정보를_가져온다() {
        // given
        List<MemberFCM> memberFCMS = List.of(
            new MemberFCM(1L, 1L, "token"),
            new MemberFCM(2L, 1L, "token2")
        );
        given(memberFCMRepository.findByMemberId(anyLong()))
            .willReturn(memberFCMS);

        List<MemberFCMResponse> expect = memberFCMS.stream()
            .map(MemberFCMResponse::from)
            .collect(Collectors.toList());

        // when
        List<MemberFCMResponse> actual = memberFCMService.findMemberFCM(1L).memberFCMs();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 기존_유저의_새로운_FCM_토큰이라면_저장() {
        // given
        String accessToken = "accessToken";
        String fcmToken = "fcmToken";
        Long memberId = 1L;
        given(authExtractor.extract(any()))
            .willReturn(new AuthPayload(memberId, Role.MEMBER));
        given(memberFCMRepository.findMemberFCMByMemberIdAndFcmToken(memberId, fcmToken))
            .willReturn(Optional.empty());

        // when
        memberFCMService.saveOriginMemberFCM(accessToken, fcmToken);

        // then
        verify(memberFCMRepository, times(1))
            .save(any(MemberFCM.class));
    }

    @Test
    void 기존_유저의_이미_존재하는_유저의_FCM_토큰이라면_저장하지_않는다() {
        // given
        String accessToken = "accessToken";
        String originToken = "fcmToken";
        Long memberId = 1L;
        given(authExtractor.extract(any()))
            .willReturn(new AuthPayload(memberId, Role.MEMBER));
        given(memberFCMRepository.findMemberFCMByMemberIdAndFcmToken(memberId, originToken))
            .willReturn(Optional.of(new MemberFCM(memberId, originToken)));

        // when
        memberFCMService.saveOriginMemberFCM(accessToken, originToken);

        // then
        verify(memberFCMRepository, never())
            .save(any(MemberFCM.class));
    }

    @Test
    void 새로운_유저의_FCM_토큰을_저장한다() {
        // given
        String accessToken = "accessToken";
        String fcmToken = "fcmToken";
        Long memberId = 1L;
        given(authExtractor.extract(any()))
            .willReturn(new AuthPayload(memberId, Role.MEMBER));

        // when
        memberFCMService.saveNewMemberFCM(accessToken, fcmToken);

        // then
        verify(memberFCMRepository, times(1))
            .save(any(MemberFCM.class));
    }

    @Test
    void 유저의_FCM_삭제() {
        // given
        Long memberId = 1L;

        // when
        memberFCMService.deleteMemberFCM(memberId);

        // then
        verify(memberFCMRepository, times(1))
            .deleteAllByMemberId(memberId);
    }
}
