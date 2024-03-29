package com.festago.bookmark.presentation.v1;

import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.bookmark.application.FestivalBookmarkV1QueryService;
import com.festago.bookmark.dto.FestivalBookmarkV1Response;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.SchoolV1Response;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalBookmarkV1ControllerTest {

    private static final String TOKEN = "Bearer token";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FestivalBookmarkV1QueryService festivalBookmarkV1QueryService;

    @Nested
    class 축제_북마크_축제_식별자_목록_조회 {

        final String uri = "/api/v1/bookmarks/festivals/ids";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답과_북마크한_축제_식별자_목록이_반환된다() throws Exception {
                // given
                given(festivalBookmarkV1QueryService.findBookmarkedFestivalIds(anyLong()))
                    .willReturn(List.of(1L, 2L, 3L));

                // when & then
                mockMvc.perform(get(uri)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").value(contains(1, 2, 3)));
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri))
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class 축제_북마크_축제_목록_조회 {

        final String uri = "/api/v1/bookmarks/festivals";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 식별자_목록과_정렬_기준_요청을_보내면_200_응답과_축제_목록이_반환된다() throws Exception {
                // given
                given(festivalBookmarkV1QueryService.findBookmarkedFestivals(anyLong(), anyList(), any()))
                    .willReturn(List.of(
                        createFestivalV1Response(1L, "테코대학교 봄 축제"),
                        createFestivalV1Response(2L, "테코대학교 여름 축제"),
                        createFestivalV1Response(3L, "테코대학교 가을 축제")
                    ));

                // when & then
                mockMvc.perform(get(uri)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .queryParam("festivalIds", "1,2,3")
                        .queryParam("festivalBookmarkOrder", "FESTIVAL"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[*].festival.id").value(contains(1, 2, 3)));
            }

            private FestivalBookmarkV1Response createFestivalV1Response(Long festivalId, String festivalName) {
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now();
                return new FestivalBookmarkV1Response(
                    new FestivalV1Response(
                        festivalId,
                        festivalName,
                        startDate,
                        endDate,
                        "https://image.com/posterImage.png",
                        new SchoolV1Response(1L, "테코대학교"),
                        "[]"
                    ),
                    LocalDateTime.now()
                );
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri))
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class 축제_북마크_등록 {

        final String uri = "/api/v1/bookmarks/festivals/{festivalId}";

        Long festivalId = 1L;

        @Nested
        @DisplayName("PUT " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(put(uri, festivalId)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(put(uri, festivalId))
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class 축제_북마크_삭제 {

        final String uri = "/api/v1/bookmarks/festivals/{festivalId}";

        Long festivalId = 1L;

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, festivalId)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN))
                    .andExpect(status().isNoContent());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, festivalId))
                    .andExpect(status().isUnauthorized());
            }
        }
    }
}
