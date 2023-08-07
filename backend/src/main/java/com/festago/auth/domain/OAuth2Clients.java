package com.festago.auth.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OAuth2Clients {

    private final Map<SocialType, Supplier<OAuth2Client>> oAuth2ClientMap;

    private OAuth2Clients(Map<SocialType, Supplier<OAuth2Client>> oAuth2ClientMap) {
        this.oAuth2ClientMap = oAuth2ClientMap;
    }

    public static OAuth2ClientsBuilder builder() {
        return new OAuth2ClientsBuilder();
    }

    public OAuth2Client getClient(SocialType socialType) {
        return oAuth2ClientMap.getOrDefault(socialType, () -> {
            throw new BadRequestException(ErrorCode.OAUTH2_NOT_SUPPORTED_SOCIAL_TYPE);
        }).get();
    }

    public static class OAuth2ClientsBuilder {

        private final Map<SocialType, Supplier<OAuth2Client>> oAuth2ClientMap = new EnumMap<>(SocialType.class);

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
                throw new InternalServerException(ErrorCode.DUPLICATE_SOCIAL_TYPE);
            }
            oAuth2ClientMap.put(socialType, () -> oAuth2Client);
            return this;
        }

        public OAuth2Clients build() {
            return new OAuth2Clients(oAuth2ClientMap);
        }
    }
}
