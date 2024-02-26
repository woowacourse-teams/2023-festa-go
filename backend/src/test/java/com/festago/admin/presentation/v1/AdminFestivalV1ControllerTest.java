package com.festago.admin.presentation.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.dto.FestivalV1UpdateRequest;
import com.festago.admin.presentation.v1.dto.StageV1CreateRequest;
import com.festago.admin.presentation.v1.dto.StageV1UpdateRequest;
import com.festago.auth.domain.Role;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.stage.application.command.StageCommandFacadeService;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminFestivalV1ControllerTest {

    private static final Cookie TOKEN_COOKIE = new Cookie("token", "token");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FestivalCommandFacadeService festivalCommandFacadeService;

    @Autowired
    StageCommandFacadeService stageCommandFacadeService;

    @Nested
    class 축제_생성 {

        final String uri = "/admin/api/v1/festivals";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            String name = "테코대학교 축제";
            LocalDate startDate = LocalDate.parse("2024-01-31");
            LocalDate endDate = LocalDate.parse("2024-02-01");
            String thumbnail = "https://image.com/image.png";
            FestivalCreateRequest request = new FestivalCreateRequest(name, startDate, endDate, thumbnail, 1L);

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_201_응답과_Location_헤더에_식별자가_반환된다() throws Exception {
                // given
                given(festivalCommandFacadeService.createFestival(any(FestivalCreateCommand.class)))
                    .willReturn(1L);

                // when & then
                mockMvc.perform(post(uri)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "/admin/api/v1/festivals/1"));
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(post(uri))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(post(uri)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 축제_수정 {

        final String uri = "/admin/api/v1/festivals/{festivalId}";

        @Nested
        @DisplayName("PATCH " + uri)
        class 올바른_주소로 {

            String name = "테코대학교 축제";
            LocalDate startDate = LocalDate.parse("2024-01-31");
            LocalDate endDate = LocalDate.parse("2024-02-01");
            String thumbnail = "https://image.com/image.png";
            FestivalV1UpdateRequest request = new FestivalV1UpdateRequest(name, startDate, endDate, thumbnail);

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, 1L)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, 1L))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, 1L)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 축제_삭제 {

        final String uri = "/admin/api/v1/festivals/{festivalId}";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1L)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNoContent());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1L))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1L)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 공연_생성 {

        final String uri = "/admin/api/v1/festivals/{festivalId}/stages";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            LocalDateTime startTime = LocalDateTime.parse("2077-06-30T18:00:00");
            LocalDateTime ticketOpenTime = LocalDateTime.parse("2077-06-23T00:00:00");
            List<Long> artistIds = List.of(1L, 2L, 3L);
            StageV1CreateRequest request = new StageV1CreateRequest(startTime, ticketOpenTime, artistIds);

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_201_응답과_Location_헤더에_식별자가_반환된다() throws Exception {
                // given
                given(stageCommandFacadeService.createStage(any(StageCreateCommand.class)))
                    .willReturn(1L);

                // when & then
                mockMvc.perform(post(uri, 1)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "/admin/api/v1/festivals/1/stages/1"));
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(post(uri, 1))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(post(uri, 1)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 공연_수정 {

        final String uri = "/admin/api/v1/festivals/{festivalId}/stages/{stageId}";

        @Nested
        @DisplayName("PATCH " + uri)
        class 올바른_주소로 {

            LocalDateTime startTime = LocalDateTime.parse("2077-06-30T18:00:00");
            LocalDateTime ticketOpenTime = LocalDateTime.parse("2077-06-23T00:00:00");
            List<Long> artistIds = List.of(1L, 2L, 3L);
            StageV1UpdateRequest request = new StageV1UpdateRequest(startTime, ticketOpenTime, artistIds);

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, 1, 1)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, 1, 1))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, 1, 1)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 공연_삭제 {

        final String uri = "/admin/api/v1/festivals/{festivalId}/stages/{stageId}";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1, 1)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNoContent());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1, 1))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1, 1)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }
}
