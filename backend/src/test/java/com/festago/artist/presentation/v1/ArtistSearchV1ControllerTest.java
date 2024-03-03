package com.festago.artist.presentation.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.application.ArtistV1SearchQueryService;
import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.support.CustomWebMvcTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistSearchV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArtistV1SearchQueryService artistV1SearchQueryService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 아티스트_검색_조회 {

        final String uri = "/api/v1/search/artists";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_body가_반환된다() throws Exception {
                // given
                var expected = List.of(
                    new ArtistSearchResponse(1L, "블랙핑크", "www.profileImage.png"),
                    new ArtistSearchResponse(2L, "에이핑크", "www.profileImage.png")
                );
                given(artistV1SearchQueryService.findAllByKeyword("핑크"))
                    .willReturn(expected);

                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("keyword", "핑크"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));
            }

            @ParameterizedTest
            @NullAndEmptySource
            void 키워드가_빈값이거나_null이면_400을_반환한다(String keyword) throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("keyword", keyword))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
            }
        }
    }
}
