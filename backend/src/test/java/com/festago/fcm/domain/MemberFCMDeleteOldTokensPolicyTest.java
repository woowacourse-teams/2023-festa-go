package com.festago.fcm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.fcm.repository.MemoryMemberFCMRepository;
import com.festago.support.fixture.MemberFCMFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberFCMDeleteOldTokensPolicyTest {

    MemberFCMDeleteOldTokensPolicy memberFCMDeleteOldTokensPolicy;

    MemberFCMRepository memberFCMRepository;

    Long memberId = 1L;

    @BeforeEach
    void setUp() {
        memberFCMRepository = new MemoryMemberFCMRepository();
        memberFCMDeleteOldTokensPolicy = new MemberFCMDeleteOldTokensPolicy(memberFCMRepository);
    }

    @Nested
    class 오래된_FCM_토큰의_삭제_정책은 {

        @ParameterizedTest
        @ValueSource(ints = {2, 3, 4})
        void 모든_토큰을_삭제한다(int tokenSize) {
            // given
            List<MemberFCM> memberFCMs = IntStream.rangeClosed(0, tokenSize)
                .mapToObj(i -> MemberFCMFixture.builder()
                    .memberId(memberId)
                    .fcmToken("oldToken" + i)
                    .expiredAt(LocalDateTime.parse("2077-06-29T18:00:00").plusMinutes(i))
                    .build())
                .toList();
            memberFCMs.forEach(memberFCMRepository::save);

            // when
            memberFCMDeleteOldTokensPolicy.delete(memberFCMs);

            // then
            List<MemberFCM> actual = memberFCMRepository.findAllByMemberId(memberId);
            assertThat(actual).isEmpty();
        }
    }
}
