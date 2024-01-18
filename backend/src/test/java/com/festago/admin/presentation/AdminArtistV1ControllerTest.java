package com.festago.admin.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.application.ArtistQueryService;
import com.festago.artist.dto.ArtistCreateRequest;
import com.festago.artist.dto.ArtistResponse;
import com.festago.artist.dto.ArtistUpdateRequest;
import com.festago.auth.domain.Role;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminArtistV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ArtistQueryService artistQueryService;

    @Autowired
    ArtistCommandService artistCommandService;

    private static final String API_URL = "/admin/api/v1/artists";
    private static final Cookie COOKIE = new Cookie("token", "token");

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 아티스트_생성_성공시_location_header와_201_created_응답() throws Exception {
        // given
        ArtistCreateRequest request = new ArtistCreateRequest("윤하", "www.naver.com");
        given(artistCommandService.save(any(ArtistCreateRequest.class)))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post(API_URL)
                        .cookie(COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/1"));
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 아티스트_수정_성공시_200_응답() throws Exception {
        // given
        ArtistUpdateRequest request = new ArtistUpdateRequest("윤하", "www.naver.com");

        // when & then
        mockMvc.perform(put(API_URL + "/1")
                        .cookie(COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 아티스트_단일_조회_성공시_200_응답() throws Exception {
        // given
        ArtistResponse expected = new ArtistResponse(1L, "윤하", "www.naver.com");
        given(artistQueryService.findById(expected.id()))
                .willReturn(expected);

        // when & then
        mockMvc.perform(get(API_URL + "/1")
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 아티스트_전체_조회_성공시_200_응답() throws Exception {
        // given
        List<ArtistResponse> expected = List.of(
                new ArtistResponse(1L, "윤하", "www.naver.com"),
                new ArtistResponse(2L, "고윤하", "www.daum.com")
        );
        given(artistQueryService.findAll())
                .willReturn(expected);

        // when & then
        mockMvc.perform(get(API_URL)
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
