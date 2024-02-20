package com.festago.admin.presentation.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.dto.ArtistCreateRequest;
import com.festago.admin.dto.ArtistUpdateRequest;
import com.festago.admin.dto.ArtistV1Response;
import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.application.ArtistV1QueryService;
import com.festago.auth.domain.Role;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminArtistV1ControllerTest {

    private static final Cookie COOKIE = new Cookie("token", "token");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ArtistV1QueryService artistV1QueryService;
    @Autowired
    ArtistCommandService artistCommandService;

    @Nested
    class 아티스트_생성 {

        final String uri = "/admin/api/v1/artists";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_201_응답과_Location_헤더에_식별자가_반환된다() throws Exception {
                // given
                ArtistCreateRequest request = new ArtistCreateRequest("윤서연", "https://image.com/image.png");
                given(artistCommandService.save(any(ArtistCreateRequest.class)))
                    .willReturn(1L);

                // when & then
                mockMvc.perform(post(uri)
                        .cookie(COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, uri + "/1"));
            }
        }
    }

    @Nested
    class 아티스트_수정 {

        final String uri = "/admin/api/v1/artists/{artistId}";

        @Nested
        @DisplayName("PUT " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                ArtistUpdateRequest request = new ArtistUpdateRequest("윤하", "https://image.com/image.png");

                // when & then
                mockMvc.perform(put(uri, 1L)
                        .cookie(COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class 아티스트_삭제 {

        final String uri = "/admin/api/v1/artists/{artistId}";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1L)
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            }
        }
    }

    @Nested
    class 아티스트_단일_조회 {

        final String uri = "/admin/api/v1/artists/{artistId}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답과_body가_반환된다() throws Exception {
                // given
                ArtistV1Response expected = new ArtistV1Response(1L, "윤하", "https://image.com/image.png");
                given(artistV1QueryService.findById(expected.id()))
                    .willReturn(expected);

                // when & then
                mockMvc.perform(get(uri, 1L)
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));
            }
        }
    }

    @Nested
    class 아티스트_전체_조회 {

        final String uri = "/admin/api/v1/artists";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답과_body가_반환된다() throws Exception {
                // given
                List<ArtistV1Response> expected = List.of(
                    new ArtistV1Response(1L, "윤하", "https://image.com/image1.png"),
                    new ArtistV1Response(2L, "고윤하", "https://image.com/image2.png")
                );
                given(artistV1QueryService.findAll())
                    .willReturn(expected);

                // when & then
                mockMvc.perform(get(uri)
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));
            }
        }
    }
}
