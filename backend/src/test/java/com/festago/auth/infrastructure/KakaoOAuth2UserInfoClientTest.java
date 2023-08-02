package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.KakaoUserInfo;
import com.festago.auth.dto.KakaoUserInfo.KakaoAccount;
import com.festago.auth.dto.KakaoUserInfo.KakaoAccount.Profile;
import com.festago.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RestClientTest(KakaoOAuth2UserInfoClient.class)
class KakaoOAuth2UserInfoClientTest {

    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    KakaoOAuth2UserInfoClient kakaoOAuth2UserInfoClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 예상치_못한_예외면_서버_에러() {
        // given
        mockServer.expect(requestTo(USER_INFO_URL))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2UserInfoClient.getUserInfo("accessToken"))
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
        UserInfo actual = kakaoOAuth2UserInfoClient.getUserInfo("accessToken");

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
