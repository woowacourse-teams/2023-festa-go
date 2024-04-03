package com.festago.artist.presentation.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.application.ArtistDetailV1QueryService;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.festago.artist.dto.ArtistMediaV1Response;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.support.CustomWebMvcTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistDetailV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArtistDetailV1QueryService artistDetailV1QueryService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 아티스트_상세_조회 {

        final String uri = "/api/v1/artists/{artistId}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_body가_반환된다() throws Exception {
                // given
                var expected = new ArtistDetailV1Response(
                    1L, "경북대학교",
                    "https://image.com/logo.png",
                    "https://image.com/backgroundLogo.png",
                    List.of(
                        new ArtistMediaV1Response(SocialMediaType.YOUTUBE.name(), "유튜브",
                            "https://image.com/youtube.png", "www.knu-youtube.com"),
                        new ArtistMediaV1Response(SocialMediaType.INSTAGRAM.name(), "인스타그램",
                            "https://image.com/youtube.png", "www.knu-instagram.com")
                    )
                );
                given(artistDetailV1QueryService.findArtistDetail(expected.id()))
                    .willReturn(expected);

                // when & then
                mockMvc.perform(get(uri, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            }
        }
    }


    @Nested
    class 아티스트별_축제_조회 {

        final String uri = "/api/v1/artists/{artistId}/festivals";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_body가_반환된다() throws Exception {
                // given
                var today = LocalDate.now();
                var content = List.of(new ArtistFestivalDetailV1Response(
                    1L, "경북대학교", today, today.plusDays(1), "www.image.com/image.png",
                    "아티스트"
                ));
                var slice = new SliceImpl<>(content, Pageable.ofSize(10), true);
                given(artistDetailV1QueryService.findArtistFestivals(1L, null, null, false, Pageable.ofSize(10)))
                    .willReturn(slice);

                // when & then
                mockMvc.perform(get(uri, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            }

            @Test
            void 요청시_페이지가_20을_넘어가면_예외() throws Exception {
                // given
                int maxPageSize = 20;

                // when && then
                mockMvc.perform(get(uri, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", String.valueOf(maxPageSize + 1)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
            }
        }
    }
}
