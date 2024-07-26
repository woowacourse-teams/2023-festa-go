package com.festago.festival.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.fixture.FestivalFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalTest {

    @Nested
    class getImageUrls {

        @Test
        void 가지고_있는_모든_이미지_URL을_반환한다() {
            // given
            Festival festival = FestivalFixture.builder().build();

            // when
            List<String> imageUrls = festival.getImageUrls();

            // then
            assertThat(imageUrls)
                .containsExactlyInAnyOrder(festival.getPosterImageUrl());
        }
    }
}
