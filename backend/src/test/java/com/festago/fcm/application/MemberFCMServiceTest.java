package com.festago.fcm.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.MemberFCMResponse;
import com.festago.fcm.repository.MemoryMemberFCMRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberFCMServiceTest {

    MemoryMemberFCMRepository memberFCMRepository = new MemoryMemberFCMRepository();

    AuthExtractor authExtractor = mock();

    MemberFCMService memberFCMService = new MemberFCMService(
        memberFCMRepository,
        authExtractor
    );

    @BeforeEach
    void setUp() {
        memberFCMRepository.clear();
        reset(authExtractor);
    }

    @Nested
    class 사용자의_모든_FCM_토큰_조회 {

        @Test
        void 성공() {
            // given
            MemberFCM firstMemberFCM = new MemberFCM(1L, "token1");
            memberFCMRepository.save(firstMemberFCM);

            MemberFCM secondMemberFCM = new MemberFCM(1L, "token2");
            memberFCMRepository.save(secondMemberFCM);

            // when
            List<MemberFCMResponse> actual = memberFCMService.findMemberFCM(1L).memberFCMs();

            // then
            assertThat(actual)
                .map(MemberFCMResponse::fcmToken)
                .contains(
                    firstMemberFCM.getFcmToken(),
                    secondMemberFCM.getFcmToken()
                ).hasSize(2);
        }
    }

    @Nested
    class 사용자의_FCM_토큰_저장 {

        @Test
        void 기존_유저의_새로운_FCM_토큰이라면_저장한다() {
            // given
            String accessToken = "accessToken";
            String fcmToken = "fcmToken";
            boolean isNewMember = false;
            Long memberId = 1L;

            given(authExtractor.extract(any()))
                .willReturn(new AuthPayload(memberId, Role.MEMBER));

            // when
            memberFCMService.saveMemberFCM(isNewMember, accessToken, fcmToken);

            // then
            assertThat(memberFCMRepository.findByMemberIdAndFcmToken(memberId, fcmToken))
                .isPresent();
        }

        @Test
        void 이미_존재하는_유저의_FCM_토큰이라면_저장하지_않는다() {
            // given
            String accessToken = "accessToken";
            String fcmToken = "fcmToken";
            boolean isNewMember = false;
            Long memberId = 1L;

            given(authExtractor.extract(any()))
                .willReturn(new AuthPayload(memberId, Role.MEMBER));

            memberFCMRepository.save(new MemberFCM(memberId, fcmToken));

            // when
            memberFCMService.saveMemberFCM(isNewMember, accessToken, fcmToken);

            // then
            assertThat(memberFCMRepository.findByMemberId(memberId))
                .hasSize(1);
        }

        @Test
        void 새로운_유저의_FCM_토큰을_저장한다() {
            // given
            String accessToken = "accessToken";
            String fcmToken = "fcmToken";
            boolean isNewMember = true;
            Long memberId = 1L;

            given(authExtractor.extract(any()))
                .willReturn(new AuthPayload(memberId, Role.MEMBER));

            // when
            memberFCMService.saveMemberFCM(isNewMember, accessToken, fcmToken);

            // then
            assertThat(memberFCMRepository.findByMemberId(memberId))
                .hasSize(1);
        }
    }

    @Nested
    class 사용자의_FCM_토큰_삭제 {

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            String fcmToken = "fcmToken";
            memberFCMRepository.save(new MemberFCM(memberId, fcmToken));

            // when
            memberFCMService.deleteMemberFCM(memberId);

            // then
            assertThat(memberFCMRepository.findByMemberId(memberId))
                .isEmpty();
        }
    }
}
