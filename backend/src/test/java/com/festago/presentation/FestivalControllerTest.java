package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.festival.application.FestivalService;
import com.festago.festival.dto.FestivalDetailResponse;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.dto.FestivalsResponse;
import com.festago.festival.repository.FestivalFilter;
import com.festago.support.CustomWebMvcTest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FestivalService festivalService;

    @Captor
    ArgumentCaptor<FestivalFilter> festivalFilterCaptor;

    @Test
    void 축제를_조회한다() throws Exception {
        // given
        FestivalResponse festivalResponse1 = new FestivalResponse(1L, 1L, "테코대학교", LocalDate.now(),
            LocalDate.now().plusDays(3), "https://image1.png");
        FestivalResponse festivalResponse2 = new FestivalResponse(2L, 2L, "우테대학교", LocalDate.now().minusDays(3),
            LocalDate.now(), "https://image2.png");
        FestivalsResponse expected = new FestivalsResponse(List.of(festivalResponse1, festivalResponse2));
        given(festivalService.findFestivals(any(FestivalFilter.class)))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/festivals")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        FestivalsResponse actual = objectMapper.readValue(content, FestivalsResponse.class);
        assertSoftly(softAssertions -> {
                verify(festivalService, times(1)).findFestivals(festivalFilterCaptor.capture());
                assertThat(festivalFilterCaptor.getValue()).isEqualTo(FestivalFilter.PROGRESS);
                assertThat(actual).isEqualTo(expected);
            }
        );
    }

    @Test
    void 축제_정보_상세_조회() throws Exception {
        // given
        FestivalDetailResponse expected = new FestivalDetailResponse(1L, 1L, "테코 대학교", LocalDate.now(), LocalDate.now(),
            "thumbnail.png", Collections.emptyList());

        given(festivalService.findDetail(anyLong()))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/festivals/{festivalId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        FestivalDetailResponse actual = objectMapper.readValue(content, FestivalDetailResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
