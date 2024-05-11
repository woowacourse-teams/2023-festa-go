package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.festago.auth.infrastructure.openid.CachedOpenIdKeyProvider;
import io.jsonwebtoken.security.JwkSet;
import io.jsonwebtoken.security.Jwks;
import java.security.Key;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CachedOpenIdKeyProviderTest {

    CachedOpenIdKeyProvider cachedOpenIdKeyProvider;

    String jwksJson = """
        {
            "keys": [
                {
                    "kid": "1",
                    "kty": "RSA",
                    "alg": "RS256",
                    "use": "sig",
                    "n": "q8zZ0b_MNaLd6Ny8wd4cjFomilLfFIZcmhNSc1ttx_oQdJJZt5CDHB8WWwPGBUDUyY8AmfglS9Y1qA0_fxxs-ZUWdt45jSbUxghKNYgEwSutfM5sROh3srm5TiLW4YfOvKytGW1r9TQEdLe98ork8-rNRYPybRI3SKoqpci1m1QOcvUg4xEYRvbZIWku24DNMSeheytKUz6Ni4kKOVkzfGN11rUj1IrlRR-LNA9V9ZYmeoywy3k066rD5TaZHor5bM5gIzt1B4FmUuFITpXKGQZS5Hn_Ck8Bgc8kLWGAU8TzmOzLeROosqKE0eZJ4ESLMImTb2XSEZuN1wFyL0VtJw",
                    "e": "AQAB"
                }
            ]
        }
        """;

    @BeforeEach
    void setUp() {
        cachedOpenIdKeyProvider = new CachedOpenIdKeyProvider();
    }

    @Test
    void kid에_대한_JWK_키가_있으면_null이_아니다() {
        // given
        JwkSet jwtSet = Jwks.setParser()
            .build()
            .parse(jwksJson);

        // when
        Key actual = cachedOpenIdKeyProvider.provide("1", () -> jwtSet);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void kid에_대한_JWK_키가_없으면_null이다() {
        // given
        JwkSet jwtSet = Jwks.setParser()
            .build()
            .parse(jwksJson);

        // when
        Key actual = cachedOpenIdKeyProvider.provide("2", () -> jwtSet);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void kid에_대한_JWK_키가_캐싱되어야_한다() {
        // given
        JwkSet jwtSet = Jwks.setParser()
            .build()
            .parse(jwksJson);
        Supplier<JwkSet> jwkSetSupplier = mock(Supplier.class);
        given(jwkSetSupplier.get())
            .willReturn(jwtSet);
        cachedOpenIdKeyProvider.provide("1", jwkSetSupplier);

        // when
        cachedOpenIdKeyProvider.provide("1", jwkSetSupplier);

        // then
        verify(jwkSetSupplier, times(1)).get();
    }

    @Test
    void 동시에_요청이_와도_캐시에_값을_갱신하는_로직은_한_번만_호출된다() {
        // given
        JwkSet jwtSet = Jwks.setParser()
            .build()
            .parse(jwksJson);
        Supplier<JwkSet> jwkSetSupplier = mock(Supplier.class);
        given(jwkSetSupplier.get())
            .willReturn(jwtSet);

        // when
        var futures = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> CompletableFuture.runAsync(() -> cachedOpenIdKeyProvider.provide("1", jwkSetSupplier)))
            .toList();
        futures.forEach(CompletableFuture::join);

        // then
        verify(jwkSetSupplier, times(1)).get();
    }
}
