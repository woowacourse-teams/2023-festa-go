package com.festago.admin.presentation.v1;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.domain.Role;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import com.festago.upload.application.ImageFileUploadService;
import com.festago.upload.dto.FileUploadResult;
import jakarta.servlet.http.Cookie;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminUploadV1ControllerTest {

    private static final Cookie TOKEN_COOKIE = new Cookie("token", "token");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ImageFileUploadService imageFileUploadService;

    @Nested
    class 이미지_업로드 {

        final String uri = "/admin/api/v1/upload/images";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                UUID fileId = UUID.randomUUID();
                URI uploadUri = URI.create("https://festago.com/" + fileId + ".png");
                given(imageFileUploadService.upload(any(), any(), any()))
                    .willReturn(new FileUploadResult(fileId, uploadUri));
                MockMultipartFile multipartFile = new MockMultipartFile(
                    "image", "image.png", "png", "data".getBytes(StandardCharsets.UTF_8)
                );

                // when & then
                mockMvc.perform(multipart(uri)
                        .file(multipartFile)
                        .cookie(TOKEN_COOKIE))
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
}
