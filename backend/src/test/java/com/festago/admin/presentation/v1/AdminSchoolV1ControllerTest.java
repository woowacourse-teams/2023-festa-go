package com.festago.admin.presentation.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.application.AdminSchoolV1QueryService;
import com.festago.admin.dto.school.AdminSchoolV1Response;
import com.festago.admin.dto.school.SchoolV1CreateRequest;
import com.festago.admin.dto.school.SchoolV1UpdateRequest;
import com.festago.auth.domain.Role;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.application.SchoolDeleteService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
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
class AdminSchoolV1ControllerTest {

    private static final Cookie TOKEN_COOKIE = new Cookie("token", "token");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    SchoolDeleteService schoolDeleteService;

    @Autowired
    AdminSchoolV1QueryService adminSchoolV1QueryService;

    @Nested
    class 학교_생성 {

        final String uri = "/admin/api/v1/schools";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_201_응답과_Location_헤더에_식별자가_반환된다() throws Exception {
                // given
                var request = SchoolV1CreateRequest.builder()
                    .name("테코대학교")
                    .domain("teco.ac.kr")
                    .region(SchoolRegion.서울)
                    .logoUrl("https://image.com/logo.png")
                    .backgroundImageUrl("https://image.com/backgroundImage.png")
                    .build();
                given(schoolCommandService.createSchool(any(SchoolCreateCommand.class)))
                    .willReturn(1L);

                // when & then
                mockMvc.perform(post(uri)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "/api/v1/schools/1"));
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
    class 학교_수정 {

        final String uri = "/admin/api/v1/schools/{schoolId}";

        @Nested
        @DisplayName("PATCH " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = SchoolV1UpdateRequest.builder()
                    .name("테코대학교")
                    .domain("teco.ac.kr")
                    .region(SchoolRegion.서울)
                    .logoUrl("https://image.com/logo.png")
                    .backgroundImageUrl("https://image.com/backgroundImage.png")
                    .build();

                // when & then
                mockMvc.perform(patch(uri, 1L)
                        .cookie(new Cookie("token", "Bearer token"))
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
    class 학교_삭제 {

        final String uri = "/admin/api/v1/schools/{schoolId}";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1L)
                        .cookie(new Cookie("token", "Bearer token"))
                        .contentType(MediaType.APPLICATION_JSON))
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
    class 모든_학교_정보_조회 {

        final String uri = "/admin/api/v1/schools";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_하면_200_응답과_학교_정보_목록이_반환된다() throws Exception {
                // given
                var expected = List.of(
                    new AdminSchoolV1Response(
                        1L,
                        "teco.ac.kr",
                        "테코대학교",
                        SchoolRegion.서울,
                        "https://image.com/logo.png",
                        "https://image.com/backgroundImage.png",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    )
                );
                given(adminSchoolV1QueryService.findAll(any(SearchCondition.class)))
                    .willReturn(new PageImpl<>(expected));

                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", "Bearer token")))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class 단일_학교_정보_조회 {

        final String uri = "/admin/api/v1/schools/{schoolId}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_하면_200_응답과_학교_정보가_반환된다() throws Exception {
                // given
                var expected = new AdminSchoolV1Response(
                    1L,
                    "teco.ac.kr",
                    "테코대학교",
                    SchoolRegion.서울,
                    "https://image.com/logo.png",
                    "https://image.com/backgroundImage.png",
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
                given(adminSchoolV1QueryService.findById(anyLong()))
                    .willReturn(expected);

                // when & then
                String content = mockMvc.perform(get(uri, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", "Bearer token")))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
                var actual = objectMapper.readValue(content, AdminSchoolV1Response.class);

                assertThat(actual).isEqualTo(expected);
            }
        }
    }
}
