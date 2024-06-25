package com.festago.fcm.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.domain.MemberFCMExpiredAtPolicy;
import com.festago.fcm.domain.MemberFCMDeleteOldTokensPolicy;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.fcm.repository.MemoryMemberFCMRepository;
import com.festago.support.fixture.MemberFCMFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberFCMCommandServiceTest {

    MemberFCMCommandService memberFCMCommandService;

    MemberFCMRepository memberFCMRepository;

    MemberFCMExpiredAtPolicy memberFCMExpiredAtPolicy;

    Long memberId = 1L;

    String fcmToken = "token";

    @BeforeEach
    void setUp() {
        memberFCMRepository = new MemoryMemberFCMRepository();
        memberFCMExpiredAtPolicy = mock(MemberFCMExpiredAtPolicy.class);
        memberFCMCommandService = new MemberFCMCommandService(
            memberFCMRepository,
            memberFCMExpiredAtPolicy,
            new MemberFCMDeleteOldTokensPolicy(memberFCMRepository)
        );
    }

    @Nested
    class 사용자가_등록한_FCM_토큰이_없으면 {

        @Test
        void FCM_토큰을_등록한다() {
            // given
            given(memberFCMExpiredAtPolicy.provide())
                .willReturn(LocalDateTime.parse("2077-06-30T18:00:00"));

            // when
            memberFCMCommandService.registerFCM(memberId, fcmToken);

            // then
            assertThat(memberFCMRepository.findAllByMemberId(memberId))
                .map(MemberFCM::getFcmToken)
                .containsOnly(fcmToken);
        }
    }

    @Nested
    class 사용자가_등록한_FCM_토큰이_있고 {

        String oldToken = "oldToken";

        @BeforeEach
        void setUp() {
            memberFCMRepository.save(MemberFCMFixture.builder()
                .memberId(memberId)
                .fcmToken(oldToken)
                .expiredAt(LocalDateTime.parse("2077-06-29T18:00:00"))
                .build()
            );
        }

        @Nested
        class 요청의_FCM_토큰과_같으면 {

            @Test
            void FCM_토큰의_만료_시간을_갱신한다() {
                // given
                given(memberFCMExpiredAtPolicy.provide())
                    .willReturn(LocalDateTime.parse("2077-06-30T18:00:00"));

                // when
                memberFCMCommandService.registerFCM(memberId, oldToken);

                // then
                List<MemberFCM> memberFCMs = memberFCMRepository.findAllByMemberId(memberId);
                assertThat(memberFCMs)
                    .map(MemberFCM::getFcmToken)
                    .containsOnly(oldToken);
                assertThat(memberFCMs.get(0).getExpiredAt())
                    .isEqualTo("2077-06-30T18:00:00");
            }
        }

        @Nested
        class 요청의_FCM_토큰과_다르면 {

            @Test
            void 기존_토큰을_삭제하고_새로운_FCM_토큰을_등록한다() {
                // given
                given(memberFCMExpiredAtPolicy.provide())
                    .willReturn(LocalDateTime.parse("2077-06-30T18:00:00"));

                // when
                memberFCMCommandService.registerFCM(memberId, fcmToken);

                // then
                List<MemberFCM> fcmTokens = memberFCMRepository.findAllByMemberId(memberId);
                assertThat(fcmTokens)
                    .map(MemberFCM::getFcmToken)
                    .containsOnly(fcmToken);
            }
        }
    }
}
