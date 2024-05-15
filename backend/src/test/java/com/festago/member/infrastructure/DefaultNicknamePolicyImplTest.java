package com.festago.member.infrastructure;

import static org.assertj.core.api.Assertions.*;

import com.festago.member.domain.DefaultNicknamePolicy;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DefaultNicknamePolicyImplTest {

    @Test
    void 형용사와_명사가_합쳐진_닉네임이_반환된다() {
        // given
        DefaultNicknamePolicy defaultNicknamePolicy = new DefaultNicknamePolicyImpl(
            List.of("춤추는"),
            List.of("다람쥐")
        );

        // when
        String nickname = defaultNicknamePolicy.generate();

        // then
        assertThat(nickname).isEqualTo("춤추는 다람쥐");
    }
}
