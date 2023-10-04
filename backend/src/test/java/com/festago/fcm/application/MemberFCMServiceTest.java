package com.festago.fcm.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
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
        List<String> tokens = List.of("token1", "token2");
        given(memberFCMRepository.findAllTokenByMemberId(anyLong()))
            .willReturn(tokens);

        // when
        List<String> actual = memberFCMService.findMemberFCMTokens(1L);

        // then
        assertThat(actual).isEqualTo(tokens);
    }

    @Test
    void 유저의_FCM_저장() {
        // given
        String accessToken = "accessToken";
        String fcmToken = "fcmToken";
        given(authExtractor.extract(any()))
            .willReturn(new AuthPayload(1L, Role.MEMBER));

        // when
        memberFCMService.saveMemberFCM(accessToken, fcmToken);

        // then
        verify(memberFCMRepository, times(1))
            .save(any(MemberFCM.class));
    }
}
