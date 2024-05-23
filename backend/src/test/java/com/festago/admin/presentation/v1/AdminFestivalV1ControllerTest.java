package com.festago.admin.presentation.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.application.AdminFestivalV1QueryService;
import com.festago.admin.application.AdminStageV1QueryService;
import com.festago.admin.dto.festival.AdminFestivalDetailV1Response;
import com.festago.admin.dto.festival.AdminFestivalV1Response;
import com.festago.admin.dto.festival.FestivalV1CreateRequest;
import com.festago.admin.dto.festival.FestivalV1UpdateRequest;
import com.festago.admin.dto.stage.AdminStageArtistV1Response;
import com.festago.admin.dto.stage.AdminStageV1Response;
import com.festago.auth.domain.Role;
import com.festago.common.querydsl.SearchCondition;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
    AdminFestivalV1QueryService adminFestivalV1QueryService;

    @Autowired
    AdminStageV1QueryService adminStageV1QueryService;

    @Nested
    class 축제_생성 {

        final String uri = "/admin/api/v1/festivals";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            String name = "테코대학교 축제";
            LocalDate startDate = LocalDate.parse("2024-01-31");
            LocalDate endDate = LocalDate.parse("2024-02-01");
            String posterImageUrl = "https://image.com/image.png";
            FestivalV1CreateRequest request = new FestivalV1CreateRequest(name, startDate, endDate, posterImageUrl, 1L);

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
            String posterImageUrl = "https://image.com/image.png";
            FestivalV1UpdateRequest request = new FestivalV1UpdateRequest(name, startDate, endDate, posterImageUrl);

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
    class 모든_축제_정보_조회 {

        final String uri = "/admin/api/v1/festivals";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_하면_200_응답과_학교_정보_목록이_반환된다() throws Exception {
                // given
                var expected = List.of(
                    new AdminFestivalV1Response(1L, "테코대학교 축제", "테코대학교", LocalDate.now(), LocalDate.now(), 0)
                );
                given(adminFestivalV1QueryService.findAll(any(SearchCondition.class)))
                    .willReturn(new PageImpl<>(expected));

                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(1));
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 축제_상세_조회 {

        final String uri = "/admin/api/v1/festivals/{festivalId}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            private final Long festivalId = 1L;

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_하면_200_응답과_축제_상세_정보가_반환된다() throws Exception {
                // given
                var expected = getAdminFestivalDetailV1Response();
                given(adminFestivalV1QueryService.findDetail(anyLong()))
                    .willReturn(expected);

                // when & then
                String content = mockMvc.perform(get(uri, festivalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

                assertThat(objectMapper.readValue(content, AdminFestivalDetailV1Response.class))
                    .isEqualTo(expected);
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri, festivalId))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri, festivalId)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }

            private AdminFestivalDetailV1Response getAdminFestivalDetailV1Response() {
                String name = "테코대학교 축제";
                Long schoolId = 2L;
                String schoolName = "테코대학교";
                LocalDate startDate = LocalDate.parse("2077-06-29");
                LocalDate endDate = startDate.plusDays(2);
                String posterImageUrl = "https://image.com/image.png";
                LocalDateTime createdAt = startDate.atStartOfDay();
                LocalDateTime updatedAt = startDate.atStartOfDay();
                return new AdminFestivalDetailV1Response(
                    festivalId,
                    name,
                    schoolId,
                    schoolName,
                    startDate,
                    endDate,
                    posterImageUrl,
                    createdAt,
                    updatedAt
                );
            }
        }
    }

    @Nested
    class 축제_공연_목록_조회 {

        final String uri = "/admin/api/v1/festivals/{festivalId}/stages";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            private final Long festivalId = 1L;

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_하면_200_응답과_축제에_속한_공연_목록_정보가_반환된다() throws Exception {
                var expected = getAdminStageV1Responses();
                given(adminStageV1QueryService.findAllByFestivalId(festivalId))
                    .willReturn(expected);

                mockMvc.perform(get(uri, festivalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isOk());
            }

            private List<AdminStageV1Response> getAdminStageV1Responses() {
                return List.of(
                    new AdminStageV1Response(
                        1L,
                        LocalDateTime.now(),
                        LocalDateTime.now().minusWeeks(1),
                        List.of(
                            new AdminStageArtistV1Response(
                                1L,
                                "에픽하이"
                            ),
                            new AdminStageArtistV1Response(
                                2L,
                                "아이유"
                            )
                        ),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    ),
                    new AdminStageV1Response(
                        2L,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().minusWeeks(1),
                        List.of(
                            new AdminStageArtistV1Response(
                                3L,
                                "푸우회장"
                            )
                        ),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    )
                );
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri, festivalId))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri, festivalId)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }
}
