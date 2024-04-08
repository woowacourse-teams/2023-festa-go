package com.festago.admin.presentation.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.application.AdminSocialMediaV1QueryService;
import com.festago.admin.dto.socialmedia.AdminSocialMediaV1Response;
import com.festago.admin.dto.socialmedia.SocialMediaCreateV1Request;
import com.festago.admin.dto.socialmedia.SocialMediaUpdateV1Request;
import com.festago.auth.domain.Role;
import com.festago.socialmedia.application.SocialMediaCommandService;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.dto.command.SocialMediaCreateCommand;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminSocialMediaV1ControllerTest {

    private static final Cookie TOKEN_COOKIE = new Cookie("token", "token");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SocialMediaCommandService socialMediaCommandService;

    @Autowired
    AdminSocialMediaV1QueryService adminSocialMediaV1QueryService;

    @Nested
    class 소셜미디어_단건_조회 {

        final String uri = "/admin/api/v1/socialmedias/{socialMediaId}";
        Long socialMediaId = 1L;

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                given(adminSocialMediaV1QueryService.findById(anyLong()))
                    .willReturn(new AdminSocialMediaV1Response(
                        1L,
                        SocialMediaType.INSTAGRAM,
                        "테코대학교 총학생회 인스타그램",
                        "https://image.com/logo.png",
                        "htps://instagram.com/tecodaehak"
                    ));

                // when & then
                mockMvc.perform(get(uri, socialMediaId)
                        .cookie(TOKEN_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri, socialMediaId))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri, socialMediaId)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 소셜미디어_목록_조회 {

        final String uri = "/admin/api/v1/socialmedias";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                given(adminSocialMediaV1QueryService.findByOwnerIdAndOwnerType(anyLong(), any(OwnerType.class)))
                    .willReturn(List.of(
                        new AdminSocialMediaV1Response(
                            1L,
                            SocialMediaType.INSTAGRAM,
                            "테코대학교 총학생회 인스타그램",
                            "https://image.com/logo.png",
                            "htps://instagram.com/tecodaehak"
                        )
                    ));

                // when & then
                mockMvc.perform(get(uri)
                        .param("ownerId", "1")
                        .param("ownerType", "SCHOOL")
                        .cookie(TOKEN_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
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
    class 소셜미디어_생성 {

        final String uri = "/admin/api/v1/socialmedias";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                Long socialMediaId = 1L;
                var request = SocialMediaCreateV1Request.builder()
                    .ownerId(1L)
                    .ownerType(OwnerType.SCHOOL)
                    .socialMediaType(SocialMediaType.INSTAGRAM)
                    .url("https://instagram.com/tecodaehak")
                    .logoUrl("https://image.com/logo.png")
                    .name("테코대학교 총학생회 인스타그램")
                    .build();
                given(socialMediaCommandService.createSocialMedia(any(SocialMediaCreateCommand.class)))
                    .willReturn(socialMediaId);

                // when & then
                mockMvc.perform(post(uri)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
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
    class 소셜미디어_수정 {

        final String uri = "/admin/api/v1/socialmedias/{socialMediaId}";
        Long socialMediaId = 1L;

        @Nested
        @DisplayName("PATCH " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = SocialMediaUpdateV1Request.builder()
                    .url("https://instagram.com/tecodaehak")
                    .logoUrl("https://image.com/logo.png")
                    .name("테코대학교 총학생회 인스타그램")
                    .build();

                // when & then
                mockMvc.perform(patch(uri, socialMediaId)
                        .cookie(TOKEN_COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, socialMediaId))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(patch(uri, socialMediaId)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class 소셜미디어_삭제 {

        final String uri = "/admin/api/v1/socialmedias/{socialMediaId}";
        Long socialMediaId = 1L;

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, socialMediaId)
                        .cookie(TOKEN_COOKIE)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
            }

            @Test
            void 토큰_없이_보내면_401_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, socialMediaId))
                    .andExpect(status().isUnauthorized());
            }

            @Test
            @WithMockAuth(role = Role.MEMBER)
            void 토큰의_권한이_Admin이_아니면_404_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, socialMediaId)
                        .cookie(TOKEN_COOKIE))
                    .andExpect(status().isNotFound());
            }
        }
    }
}
