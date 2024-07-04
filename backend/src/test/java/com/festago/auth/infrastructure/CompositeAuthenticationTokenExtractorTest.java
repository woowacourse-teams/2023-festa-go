package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;

import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.infrastructure.jwt.JwtTokenParser;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CompositeAuthenticationTokenExtractorTest {

    JwtTokenParser jwtTokenParser;

    @BeforeEach
    void setUp() {
        jwtTokenParser = mock();
    }

    @Test
    void AuthenticationClaimsExtractors_모두_AnonymousAuthentication을_반환하면_권한이_Anonymous인_Authentication을_반환한다() {
        // given
        CompositeAuthenticationTokenExtractor compositeAuthenticationTokenExtractor = new CompositeAuthenticationTokenExtractor(
            jwtTokenParser,
            List.of(claims -> AnonymousAuthentication.getInstance(), claims -> AnonymousAuthentication.getInstance())
        );

        // when
        Authentication actual = compositeAuthenticationTokenExtractor.extract("token");

        // then
        assertThat(actual.getRole()).isEqualTo(Role.ANONYMOUS);
    }

    @Test
    void HttpRequestTokenExtractors_중_하나라도_AnonymousAuthentication이_아닌_값을_반환하면_해당_값을_반환한다() {
        // given
        CompositeAuthenticationTokenExtractor compositeAuthenticationTokenExtractor = new CompositeAuthenticationTokenExtractor(
            jwtTokenParser,
            List.of(claims -> AnonymousAuthentication.getInstance(), claims -> new AdminAuthentication(1L))
        );

        // when
        Authentication actual = compositeAuthenticationTokenExtractor.extract("token");

        // then
        assertThat(actual.getRole()).isEqualTo(Role.ADMIN);
    }
}
