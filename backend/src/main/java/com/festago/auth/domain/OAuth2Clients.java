package com.festago.auth.domain;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnexpectedException;
import com.festago.member.domain.SocialType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Clients {

    private final Map<SocialType, OAuth2Client> oAuth2ClientMap;

    private OAuth2Clients(Map<SocialType, OAuth2Client> oAuth2ClientMap) {
        this.oAuth2ClientMap = oAuth2ClientMap;
    }

    public static OAuth2ClientsBuilder builder() {
        return new OAuth2ClientsBuilder();
    }

    public OAuth2Client getClient(SocialType socialType) {
        return Optional.ofNullable(oAuth2ClientMap.get(socialType))
            .orElseThrow(() -> new BadRequestException(ErrorCode.OAUTH2_NOT_SUPPORTED_SOCIAL_TYPE));
    }

    public static class OAuth2ClientsBuilder {

        private final Map<SocialType, OAuth2Client> oAuth2ClientMap = new EnumMap<>(SocialType.class);

        private OAuth2ClientsBuilder() {
        }

        public OAuth2ClientsBuilder addAll(List<OAuth2Client> oAuth2Clients) {
            for (OAuth2Client oAuth2Client : oAuth2Clients) {
                add(oAuth2Client);
            }
            return this;
        }

        public OAuth2ClientsBuilder add(OAuth2Client oAuth2Client) {
            SocialType socialType = oAuth2Client.getSocialType();
            if (oAuth2ClientMap.containsKey(socialType)) {
                log.error("OAuth2 제공자는 중복될 수 없습니다.");
                throw new UnexpectedException("중복된 OAuth2 제공자 입니다.");
            }
            oAuth2ClientMap.put(socialType, oAuth2Client);
            return this;
        }

        public OAuth2Clients build() {
            return new OAuth2Clients(oAuth2ClientMap);
        }
    }
}
