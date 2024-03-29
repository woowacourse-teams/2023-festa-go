package com.festago.auth.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.auth.annotation.MemberAuth;
import com.festago.auth.domain.Role;
import com.festago.common.exception.ErrorCode;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LoginConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Nested
    class MemberAuth_어노테이션이_붙은_핸들러_메서드는_인증_기능이_수행된다 {

        @Test
        @WithMockAuth(role = Role.ANONYMOUS)
        void 토큰이_없으면_401_응답이_반환된다() throws Exception {
            mockMvc.perform(get("/api/annotation-member-auth"))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockAuth
        void 토큰이_있으면_200_응답이_반환된다() throws Exception {
            mockMvc.perform(get("/api/annotation-member-auth")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockAuth(role = Role.ADMIN)
        void 토큰의_권한이_어드민이면_404_응답이_반환된다() throws Exception {
            mockMvc.perform(get("/api/annotation-member-auth")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_ENOUGH_PERMISSION.name()));
        }
    }
}

@RestController
class AnnotationMemberAuthController {

    @MemberAuth
    @GetMapping("/api/annotation-member-auth")
    public ResponseEntity<Void> testAuthHandler() {
        return ResponseEntity.ok().build();
    }
}
