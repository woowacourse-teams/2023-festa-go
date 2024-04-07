package com.festago.bookmark.presentation.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
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
class BookmarkManagementV1ControllerTest {

    private static final String TOKEN = "Bearer token";

    @Autowired
    MockMvc mockMvc;

    @Nested
    class 북마크_등록 {

        final String uri = "/api/v1/bookmarks";

        String resourceId = "1";

        @Nested
        @DisplayName("PUT " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(put(uri)
                        .param("resourceId", resourceId)
                        .param("bookmarkType", "FESTIVAL")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(put(uri))
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class 북마크_삭제 {

        final String uri = "/api/v1/bookmarks";

        String resourceId = "1";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri)
                        .param("resourceId", resourceId)
                        .param("bookmarkType", "FESTIVAL")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN))
                    .andExpect(status().isNoContent());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri))
                    .andExpect(status().isUnauthorized());
            }
        }
    }
}
