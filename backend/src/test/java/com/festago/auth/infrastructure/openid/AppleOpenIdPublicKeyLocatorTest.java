package com.festago.auth.infrastructure.openid;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AppleOpenIdPublicKeyLocatorTest extends ApplicationIntegrationTest {

    @Autowired
    AppleOpenIdPublicKeyLocator appleOpenIdPublicKeyLocator;

    @Autowired
    KakaoOpenIdPublicKeyLocator kakaoOpenIdPublicKeyLocator;

    @Test
    void 소셜별_Locator_들은_캐싱을_공유하지_않는다() {

        // given & when & then
        assertThat(appleOpenIdPublicKeyLocator)
            .usingRecursiveComparison()
            .comparingOnlyFields("cachedOpenIdKeyProvider")
            .isNotEqualTo(kakaoOpenIdPublicKeyLocator);
    }

}
