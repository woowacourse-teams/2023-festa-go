package com.festago.bookmark.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistBookmarkV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Nested
    class 아티스트_북마크_목록_조회_에서 {

        final String uri = "/api/v1/bookmarks/artists";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답을_반환한다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            }

            @Test
            void 인증을_안했으면_4xx_응답을_반환한다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
            }
        }
    }
}
