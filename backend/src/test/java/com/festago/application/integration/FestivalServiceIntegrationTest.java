package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.application.FestivalService;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalService festivalService;

    @Test
    void 축제를_생성한다() {
        // given
        FestivalCreateRequest request = new FestivalCreateRequest("테코 대학교 축제", LocalDate.parse("2023-07-26"),
            LocalDate.parse("2023-07-28"), "thumbnail.png");

        // when
        FestivalResponse festivalResponse = festivalService.create(request);

        // then
        assertThat(festivalResponse).isNotNull();
    }
}
