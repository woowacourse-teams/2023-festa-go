package com.festago.festival.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalFilterTest {

    @Test
    void 유요하지_않은_name_이면_예외() {
        // given && when && then
        assertThatThrownBy(() -> FestivalFilter.from("unvalid"))
            .isInstanceOf(BadRequestException.class);
    }
}
