package com.festago.festival.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.festival.repository.FestivalFilter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalFilterTest {

    @Test
    void 유효하지_않은_name_이면_예외() {
        // given && when && then
        assertThatThrownBy(() -> FestivalFilter.from("unvalid"))
            .isInstanceOf(BadRequestException.class);
    }

    @ValueSource(strings = {"progress", "Progress", "PROGRESS"})
    @ParameterizedTest
    void PROGRESS_반환(String value) {
        // given && when
        FestivalFilter filter = FestivalFilter.from(value);

        // then
        assertThat(filter).isEqualTo(FestivalFilter.PROGRESS);
    }

    @ValueSource(strings = {"planned", "Planned", "PLANNED"})
    @ParameterizedTest
    void PLANNED_반환(String value) {
        // given && when
        FestivalFilter filter = FestivalFilter.from(value);

        // then
        assertThat(filter).isEqualTo(FestivalFilter.PLANNED);
    }

    @ValueSource(strings = {"end", "End", "END"})
    @ParameterizedTest
    void END_반환(String value) {
        // given && when
        FestivalFilter filter = FestivalFilter.from(value);

        // then
        assertThat(filter).isEqualTo(FestivalFilter.END);
    }
}
