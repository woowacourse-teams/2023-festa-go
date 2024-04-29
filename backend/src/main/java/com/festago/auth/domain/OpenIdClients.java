package com.festago.auth.domain;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnexpectedException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenIdClients {

    private final Map<SocialType, OpenIdClient> openIdClientMap;

    private OpenIdClients(Map<SocialType, OpenIdClient> openIdClientMap) {
        this.openIdClientMap = openIdClientMap;
    }

    public static OpenIdClientsBuilder builder() {
        return new OpenIdClientsBuilder();
    }

    public OpenIdClient getClient(SocialType socialType) {
        return Optional.ofNullable(openIdClientMap.get(socialType))
            .orElseThrow(() -> new BadRequestException(ErrorCode.OPEN_ID_NOT_SUPPORTED_SOCIAL_TYPE));
    }

    public static class OpenIdClientsBuilder {

        private final Map<SocialType, OpenIdClient> openIdClientMap = new EnumMap<>(SocialType.class);

        private OpenIdClientsBuilder() {
        }

        public OpenIdClientsBuilder addAll(List<OpenIdClient> openIdClients) {
            for (OpenIdClient openIdClient : openIdClients) {
                add(openIdClient);
            }
            return this;
        }

        public OpenIdClientsBuilder add(OpenIdClient openIdClient) {
            SocialType socialType = openIdClient.getSocialType();
            if (openIdClientMap.containsKey(socialType)) {
                log.error("OpenID 제공자는 중복될 수 없습니다.");
                throw new UnexpectedException("중복된 OpenID 제공자 입니다.");
            }
            openIdClientMap.put(socialType, openIdClient);
            return this;
        }

        public OpenIdClients build() {
            return new OpenIdClients(openIdClientMap);
        }
    }
}
