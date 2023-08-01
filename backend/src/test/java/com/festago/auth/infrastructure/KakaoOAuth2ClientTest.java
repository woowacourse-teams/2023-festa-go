package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.KakaoAccessTokenResponse;
import com.festago.auth.dto.KakaoUserInfo;
import com.festago.auth.dto.KakaoUserInfo.KakaoAccount;
import com.festago.auth.dto.KakaoUserInfo.KakaoAccount.Profile;
import com.festago.auth.infrastructure.KakaoOAuth2Client.KakaoOAuth2Error;
import com.festago.exception.BadRequestException;
import com.festago.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RestClientTest(value = KakaoOAuth2Client.class)
class KakaoOAuth2ClientTest {

    private static final String ACCESS_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    KakaoOAuth2Client kakaoOAuth2Client;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class access_token_조회시 {

        @Test
        void 전송한_코드가_잘못됐다면_예외() throws JsonProcessingException {
            // given
            KakaoOAuth2Error expected = new KakaoOAuth2Error("error", "description", "KOE320");
            mockServer.expect(requestTo(ACCESS_TOKEN_URL))
                .andRespond(MockRestResponseCreators.withBadRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(expected)));

            // when & then
            assertThatThrownBy(() -> kakaoOAuth2Client.getAccessToken("code"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("잘못된 인증 코드 입니다.");
        }

        @Test
        void 예상치못한_에러인_경우_서버_예외() throws JsonProcessingException {
            // given
            KakaoOAuth2Error expected = new KakaoOAuth2Error("error", "description", "any");
            mockServer.expect(requestTo(ACCESS_TOKEN_URL))
                .andRespond(MockRestResponseCreators.withBadRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(expected)));

            // when & then
            assertThatThrownBy(() -> kakaoOAuth2Client.getAccessToken("code"))
                .isInstanceOf(InternalServerException.class)
                .hasMessage("서버 내부에 문제가 발생했습니다.");
        }

        @Test
        void 성공() throws JsonProcessingException {
            // given
            KakaoAccessTokenResponse expected = new KakaoAccessTokenResponse("tokenType", "accessToken",
                100, "refreshToken", 50);
            mockServer.expect(requestTo(ACCESS_TOKEN_URL))
                .andRespond(MockRestResponseCreators.withSuccess()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(expected)));
            // when
            String actual = kakaoOAuth2Client.getAccessToken("code");

            // then
            assertThat(actual).isEqualTo(expected.accessToken());
        }
    }

    @Nested
    class 유저_정보_조회시 {

        @Test
        void 예상치_못한_예외면_서버_에러() {
            // given
            mockServer.expect(requestTo(USER_INFO_URL))
                .andRespond(MockRestResponseCreators.withBadRequest()
                    .contentType(MediaType.APPLICATION_JSON));

            // when & then
            assertThatThrownBy(() -> kakaoOAuth2Client.getUserInfo("accessToken"))
                .isInstanceOf(InternalServerException.class)
                .hasMessage("서버 내부에 문제가 발생했습니다.");
        }

        @Test
        void 성공() throws JsonProcessingException {
            // given
            KakaoUserInfo expected = new KakaoUserInfo("id", new KakaoAccount(new Profile("nickname", "imageUrl")));
            mockServer.expect(requestTo(USER_INFO_URL))
                .andRespond(MockRestResponseCreators.withSuccess()
                    .body(objectMapper.writeValueAsString(expected))
                    .contentType(MediaType.APPLICATION_JSON));

            // when
            UserInfo actual = kakaoOAuth2Client.getUserInfo("accessToken");

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.socialType()).isEqualTo(SocialType.KAKAO);
                softly.assertThat(actual.socialId()).isEqualTo(expected.id());
                softly.assertThat(actual.nickname()).isEqualTo(expected.kakaoAccount().profile().nickname());
                softly.assertThat(actual.profileImage())
                    .isEqualTo(expected.kakaoAccount().profile().thumbnailImageUrl());
            });
        }
    }
}
